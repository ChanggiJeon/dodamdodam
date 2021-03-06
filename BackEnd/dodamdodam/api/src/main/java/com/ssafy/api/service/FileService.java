package com.ssafy.api.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.ssafy.core.common.FileUtil;
import com.ssafy.core.entity.Family;
import com.ssafy.core.entity.Picture;
import com.ssafy.core.entity.Profile;
import com.ssafy.core.exception.ErrorCode;
import com.ssafy.core.exception.CustomException;
import com.ssafy.core.repository.FamilyRepository;
import com.ssafy.core.repository.PictureRepository;
import com.ssafy.core.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import marvin.image.MarvinImage;
import org.marvinproject.image.transform.scale.Scale;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;

import static com.ssafy.core.exception.ErrorCode.FILE_UPLOAD_FAIL;


@RequiredArgsConstructor
@Service
public class FileService {

    private final AmazonS3Client amazonS3Client;
    private final PictureRepository pictureRepository;
    private final ProfileRepository profileRepository;

    private final FamilyRepository familyRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String uploadFileV1(String category, MultipartFile file) {
        try {
            System.out.println(file.getSize());
            System.out.println(category);
            validateFileExists(file);
            checkFileNameExtension(file);

            String fileName = FileUtil.buildFileName(category, file.getOriginalFilename());
            String fileFormatName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1);

            System.out.println(fileFormatName);
            BufferedImage inputImage = ImageIO.read(file.getInputStream());

            return s3SaveAndGetUrl(fileName, fileFormatName, inputImage);

        } catch (Exception e) {
            throw new CustomException(FILE_UPLOAD_FAIL);
        }
    }

    private String s3SaveAndGetUrl(String fileName, String fileFormatName, BufferedImage inputImage) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(inputImage, fileFormatName, baos);
        baos.flush();

        MultipartFile imageFile = new MockMultipartFile(fileName, baos.toByteArray());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(imageFile.getContentType());
        objectMetadata.setContentLength(imageFile.getSize());

        InputStream inputStream = imageFile.getInputStream();

        amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return amazonS3Client.getUrl(bucketName, fileName).toString();
    }

    public void validateFileExists(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new CustomException(ErrorCode.FILE_DOES_NOT_EXIST);
        }
    }

    public void checkFileNameExtension(MultipartFile multipartFile) {
        String originFilename = Objects.requireNonNull(multipartFile.getOriginalFilename()).replace(" ", "");
        String formatName = originFilename.substring(originFilename.lastIndexOf('.') + 1).toLowerCase();
        String[] supportFormat = {"bmp", "jpg", "jpeg", "png"};
        if (!Arrays.asList(supportFormat).contains(formatName)) {
            throw new CustomException(ErrorCode.WRONG_FILE_EXTENSION);
        }
    }

    @Async
    public void resizeImage(MultipartFile file, Picture picture) {
        String filePath = resizeFile("album", file);
        picture.updatePathName(filePath);
        pictureRepository.save(picture);
    }

    @Async
    public void resizeImage(MultipartFile file, Family family) {
        String filePath = resizeFile("family", file);
        family.setPicture(filePath);
        familyRepository.save(family);

    }

    @Async
    public void resizeImage(MultipartFile file, Profile profile) {
        String filePath = resizeFile("profile", file);
        profile.updateImagePath(filePath);
        profileRepository.save(profile);

    }


    public String resizeFile(String category, MultipartFile file) {
        try {
            String fileName = FileUtil.buildResizedFileName(category, file.getOriginalFilename());
            String fileFormatName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1);
            BufferedImage inputImage = ImageIO.read(file.getInputStream());

            int originWidth = inputImage.getWidth();
            int originHeight = inputImage.getHeight();

            MarvinImage imageMarvin = new MarvinImage(inputImage);

            Scale scale = new Scale();
            scale.load();
            scale.setAttribute("newWidth", 712);
            scale.setAttribute("newHeight", 712 * originHeight / originWidth);
            scale.process(imageMarvin.clone(), imageMarvin, null, null, false);

            BufferedImage imageNoAlpha = imageMarvin.getBufferedImageNoAlpha();

            return s3SaveAndGetUrl(fileName, fileFormatName, imageNoAlpha);

        } catch (Exception e) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
    }
}

