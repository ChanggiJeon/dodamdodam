package com.ssafy.api.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.ssafy.core.common.FileUtil;
import com.ssafy.core.exception.CustomErrorCode;
import com.ssafy.core.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;


@RequiredArgsConstructor
@Service
public class FileService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public String uploadFileV1(String category, MultipartFile multipartFile) {
        validateFileExists(multipartFile);

        String fileName = FileUtil.buildFileName(category, multipartFile.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new CustomException(CustomErrorCode.FILE_SIZE_EXCEED);
        }

        return amazonS3Client.getUrl(bucketName, fileName).toString();
    }

    private void validateFileExists(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new CustomException(CustomErrorCode.FILE_DOES_NOT_EXIST);
        }
    }
    public byte[] downloadFileV1(String resourcePath) {
        validateFileExistsAtUrl(resourcePath);

        S3Object s3Object = amazonS3Client.getObject(bucketName, resourcePath);
        S3ObjectInputStream inputStream = s3Object.getObjectContent();
        try {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new CustomException(CustomErrorCode.FILE_DOWNLOAD_FAIL);
        }
    }

    private void validateFileExistsAtUrl(String resourcePath) {
        if (!amazonS3Client.doesObjectExist(bucketName, resourcePath)) {
            throw new CustomException(CustomErrorCode.FILE_DOES_NOT_EXIST);
        }
    }

    //삭제 구현        amazonS3Client.deleteObject();




//    public FileUploadResponse uploadFile(long userId, String category, List<MultipartFile> multipartFiles) {
//        List<String> fileUrls = new ArrayList<>();
//
//        // 파일 업로드 갯수를 정합니다(10개 이하로 정의)
//        for (MultipartFile multipartFile : multipartFiles) {
//            if (fileUrls.size() > 10) {
//                throw new CustomException(FILE_COUNT_EXCEED);
//            }
//
//            String fileName = PlandPMSUtils.buildFileName(userId, category, multipartFile.getOriginalFilename());
//            ObjectMetadata objectMetadata = new ObjectMetadata();
//            objectMetadata.setContentType(multipartFile.getContentType());
//
//            try (InputStream inputStream = multipartFile.getInputStream()) {
//                amazonS3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, objectMetadata)
//                        .withCannedAcl(CannedAccessControlList.PublicRead));
//                fileUrls.add(FILE_URL_PROTOCOL + bucketName + "/" + fileName);
//            } catch (IOException e) {
//                throw new CustomException(FILE_UPLOAD_FAIL);
//            }
//        }
//
//        return new FileUploadResponse(fileUrls);
//    }
}