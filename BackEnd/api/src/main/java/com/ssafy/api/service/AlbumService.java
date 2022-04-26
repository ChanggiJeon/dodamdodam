package com.ssafy.api.service;


import com.ssafy.api.dto.req.AlbumReactionReqDto;
import com.ssafy.api.dto.req.AlbumReqDto;
import com.ssafy.api.dto.res.AlbumReactionListResDto;
import com.ssafy.api.entity.*;
import com.ssafy.api.exception.CustomException;
import com.ssafy.api.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.ssafy.api.exception.CustomErrorCode.INVALID_REQUEST;

@Service
@Slf4j
@RequiredArgsConstructor
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final FamilyRepository familyRepository;
    private final HashTagRepository hashTagRepository;
    private final PictureRepository pictureRepository;
    private final AlbumReactionRepository albumReactionRepository;
    private final ProfileRepository profileRepository;


    @Transactional(readOnly = false)
    public List<Album> findAlbumsByFamilyId(long familyId){
        return albumRepository.findAlbumByFamilyId(familyId);
    }


    @Transactional(readOnly = false)
    public Album findByAlbum(long albumId){
        return albumRepository.findAlbumByAlbumId(albumId);
    }

    @Transactional(readOnly = false)
    public List<Picture> findPicturesByAlbumId(long albumId){
        return pictureRepository.findPicturesByAlbumId(albumId);
    }

    @Transactional(readOnly = false)
    public List<AlbumReaction> findAlbumReactionsByAlbumId(long albumId){
        return albumReactionRepository.findReactionsByAlbumId(albumId);
    }
    @Transactional(readOnly = false)
    public List<HashTag> findHashTagsByAlbumId(long albumId){
        return hashTagRepository.findHashTagsByAlbumId(albumId);
    }



    @Transactional(readOnly = false)
    public Family findFamilyByUserPK(Long userPK){
        Family family = familyRepository.findFamilyByUserPk(userPK);
        if (family == null) {
            throw new CustomException(INVALID_REQUEST);
        }
        System.out.println(family.toString());
        return family;
    }


    @Transactional(readOnly = false)
    public LocalDate createDate(String date){
        String[] list = date.split("-");
        LocalDate updateDate = LocalDate.of(Integer.parseInt(list[0]), Integer.parseInt(list[1]), Integer.parseInt(list[2]));
        return updateDate;
    }


    @Transactional(readOnly = false)
    public Picture findMainPictureByAlbumId(long albumId){
        return pictureRepository.findMainPictureByAlbumId(albumId);
    }


    @Transactional(readOnly = false)
    public void createAlbum(AlbumReqDto albumReqDto, Family family, Album album, List<MultipartFile> multipartFiles, HttpServletRequest request) {

//        List<MultipartFile> multipartFiles = multipartFiles.getMultipartFiles();
        String familyCode = family.getCode();
//        String[] result =  new String[multipartFiles.size()];
        List<String> hashTags = albumReqDto.getHashTags();
        try{
            String separ = File.separator;
//            String today= new SimpleDateFormat("yyMMdd").format(new Date());

            File file = new File("");
//            String rootPath = file.getAbsolutePath().split("src")[0];

//            String savePath = "../"+"profileImg"+separ+today;
            String savePath = request.getServletContext().getRealPath("/resources/Album/"+familyCode);
            if(!new File(savePath).exists()){
                try{
                    new File(savePath).mkdirs();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            String[] originFileNames  = new String[multipartFiles.size()];
            String[] realPaths =  new String[multipartFiles.size()];

            String saveFileName = "";
            String filePath = "";
            boolean isMain = false;
            for(int i = 0 ; i<multipartFiles.size() ; i++){
                originFileNames[i] = multipartFiles.get(i).getOriginalFilename();
                saveFileName = UUID.randomUUID().toString() + originFileNames[i].substring(originFileNames[i].lastIndexOf("."));
                filePath = savePath+separ+saveFileName;
                multipartFiles.get(i).transferTo(new File(filePath));
//                realPaths[i] = savePath+saveFileName;
//                result[i] = realPaths[i]+"#"+originFileNames[i];
                if(albumReqDto.getMainIndex()==i) isMain=true;
                else isMain=false;
                Picture picture = Picture.builder()
                        .album(album)
                        .origin_name(multipartFiles.get(i).getOriginalFilename())
                        .path_name(savePath+separ+saveFileName)
                        .is_main(isMain)
                        .build();
                pictureRepository.save(picture);

            }

            for (int i = 0 ; i<hashTags.size() ; i++){
                HashTag hashTag = HashTag.builder()
                        .album(album)
                        .text(hashTags.get(i))
                        .build();
                hashTagRepository.save(hashTag);

            }

//            String originFileName = multipartFile.getOriginalFilename();
//            String saveFileName = UUID.randomUUID().toString() + originFileName.substring(originFileName.lastIndexOf("."));
//
//            String filePath = savePath+separ+saveFileName;
//            multipartFile.transferTo(new File(filePath));
//            String realPath = savePath+saveFileName;
            albumRepository.save(album);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Transactional(readOnly = false)
    public void manageAlbumReaction(long userPk, Album album, AlbumReactionReqDto albumReactionReqDto){
        Profile profile = profileRepository.findProfileByUserPk(userPk);

        AlbumReaction albumReaction =
                albumReactionRepository.findReactionByAlbumId(
                        album.getId(), profile.getId());

        if(albumReaction == null){
            albumReaction = AlbumReaction.builder()
                    .profile(profile)
                    .album(album)
                    .emoticon(albumReactionReqDto.getEmoticon())
                    .build();
            albumReactionRepository.save(albumReaction);
        }else{
            albumReaction.updateEmoticon(albumReactionReqDto.getEmoticon());
            albumReactionRepository.save(albumReaction);
        }

    }

    @Transactional(readOnly = false)
    public List<AlbumReactionListResDto> findReactionList(List<AlbumReaction> albumReactions, long userPk){
        List<AlbumReactionListResDto> result = new ArrayList<>();
        Profile profile = profileRepository.findProfileByUserPk(userPk);
        for(int i = 0 ; i<albumReactions.size() ; i++) {
            AlbumReactionListResDto albumReactionListResDto = AlbumReactionListResDto.builder()
                    .emoticon(albumReactions.get(i).getEmoticon())
                    .imagePath(profile.getImagePath())
                    .role(profile.getRole())
                    .build();
            result.add(albumReactionListResDto);
        }
        return result;
    }



}
