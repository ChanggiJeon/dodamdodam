package com.ssafy.api.service;


import com.ssafy.core.dto.req.AlbumReactionReqDto;
import com.ssafy.core.dto.req.AlbumReqDto;
import com.ssafy.core.dto.req.AlbumUpdateReqDto;
import com.ssafy.core.dto.res.AlbumMainResDto;
import com.ssafy.core.dto.res.AlbumReactionListResDto;
import com.ssafy.core.entity.*;
import com.ssafy.core.exception.ErrorCode;
import com.ssafy.core.exception.CustomException;
import com.ssafy.core.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    private final FileService fileService;


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
            throw new CustomException(ErrorCode.INVALID_REQUEST);
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
    public AlbumMainResDto findMainPictureByAlbumId(long albumId){
        return pictureRepository.findMainPictureByAlbumId(albumId);
    }

    @Transactional(readOnly = false)
    public void deleteAlbum(long albumId, long userPK){
        long familyId = familyRepository.findFamilyIdByUserPk(userPK);
        Album album = albumRepository.findAlbumByAlbumId(albumId);
        if(album.getFamily().getId() != familyId){
            throw new CustomException(CustomErrorCode.NOT_BELONG_FAMILY);
        }
        albumRepository.delete(album);

    }


    @Transactional(readOnly = false)
    public void createAlbum(AlbumReqDto albumReqDto, Family family, Album album, List<MultipartFile> multipartFiles, HttpServletRequest request) {


        String familyCode = family.getCode();

        List<String> hashTags = albumReqDto.getHashTags();
        try{
//            String separ = File.separator;
//            File file = new File("");
//            String savePath = request.getServletContext().getRealPath("/resources/Album/"+familyCode);
//            if(!new File(savePath).exists()){
//                try{
//                    new File(savePath).mkdirs();
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
            String[] originFileNames  = new String[multipartFiles.size()];

            String saveFileName = "";
            String filePath = "";
            boolean isMain = false;
            for(int i = 0 ; i<multipartFiles.size() ; i++){
                originFileNames[i] = multipartFiles.get(i).getOriginalFilename();
//                saveFileName = UUID.randomUUID().toString() + originFileNames[i].substring(originFileNames[i].lastIndexOf("."));
//                filePath = savePath+separ+saveFileName;
//                multipartFiles.get(i).transferTo(new File(filePath));
                if(albumReqDto.getMainIndex()==i) isMain=true;
                else isMain=false;
                filePath = fileService.uploadFileV1("album",multipartFiles.get(i));
                Picture picture = Picture.builder()
                        .album(album)
                        .origin_name(multipartFiles.get(i).getOriginalFilename())
                        .path_name(filePath)
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
    public void updateAlbum(long userPk, Album album, AlbumUpdateReqDto albumUpdateReqDto,
                             Authentication authentication, HttpServletRequest request){
        Family family = findFamilyByUserPK(userPk);
        album.updateLocalDate(createDate(albumUpdateReqDto.getDate()));
        albumRepository.save(album);
        boolean flag;
        List<String> updateHashTags =albumUpdateReqDto.getHashTags();
        List<HashTag> hashTags = hashTagRepository.findHashTagsByAlbumId(album.getId());
        List<Picture> pictures = pictureRepository.findPicturesByAlbumId(album.getId());
        List<MultipartFile> multipartFiles = new ArrayList<>();
        if(albumUpdateReqDto.getMultipartFiles()==null){
            flag = false;
        }else{
            multipartFiles = albumUpdateReqDto.getMultipartFiles();
            flag = true;

        }
        int[] deleteIdList = albumUpdateReqDto.getPictureIdList();

        //해시태그가 기존보다 추가 된 경우
        if(updateHashTags.size()>=hashTags.size()){
            for(int i = 0;  i<updateHashTags.size() ; i++){
                if(i<hashTags.size()){
                    hashTags.get(i).updateText(updateHashTags.get(i));
                    hashTagRepository.save(hashTags.get(i));
                }
                else{
                    HashTag hashTag = HashTag.builder()
                            .album(album)
                            .text(updateHashTags.get(i))
                            .build();
                    hashTagRepository.save(hashTag);
                }
            }
        }
        //해시태그가 기존보다 줄어든 경우
        else{
            for(int i = 0 ; i<hashTags.size() ; i++){
                if(i<updateHashTags.size()){
                    hashTags.get(i).updateText(updateHashTags.get(i));
                    hashTagRepository.save(hashTags.get(i));
                }
                else{
                    hashTagRepository.delete(hashTags.get(i));
                }
            }
        }
        int mainIndex = albumUpdateReqDto.getMainIndex();
        boolean isMain = false;
        //삭제할 사진이 있는 경우
        if (deleteIdList!=null){
            for(int i = 0 ; i<deleteIdList.length ; i++){
                Picture picture = pictureRepository.findPictureByPictureId(deleteIdList[i]);
                pictureRepository.delete(picture);
            }
                //추가된 사진 저장
            if(flag) {
                for (int j = 0; j < multipartFiles.size(); j++) {
                    String originFileName = multipartFiles.get(j).getOriginalFilename();
                    String filePath = fileService.uploadFileV1("album", multipartFiles.get(j));

                    Picture picture = Picture.builder()
                            .album(album)
                            .origin_name(originFileName)
                            .path_name(filePath)
                            .build();
                    pictureRepository.save(picture);
                }
            }
            //메인 사진 업데이트
            List<Picture> newPictureList = pictureRepository.findPicturesByAlbumId(album.getId());
            for(int k = 0 ; k<newPictureList.size();k++){
                if(mainIndex==k){
                    newPictureList.get(k).updateIsMain(true);
                    break;
                }

            }
        }
        //삭제할 사진이 없는 경우
        else{
            //추가된 사진 저장
            if(flag) {
                for (int j = 0; j < multipartFiles.size(); j++) {
                    String originFileName = multipartFiles.get(j).getOriginalFilename();
                    String filePath = fileService.uploadFileV1("album", multipartFiles.get(j));

                    Picture picture = Picture.builder()
                            .album(album)
                            .origin_name(originFileName)
                            .path_name(filePath)
                            .build();
                    pictureRepository.save(picture);
                }
            }
            //메인 사진 업데이트
            List<Picture> newPictureList = pictureRepository.findPicturesByAlbumId(album.getId());
            for(int k = 0 ; k<newPictureList.size();k++){
                if(mainIndex==k){
                    newPictureList.get(k).updateIsMain(true);
                    break;
                }

            }
        }
//        //사진 수가 기존보다 많은 경우
//        if(multipartFiles.size()>=pictures.size()){
//            for(int i = 0 ; i< multipartFiles.size();i++){
//
//                String originFileName = multipartFiles.get(i).getOriginalFilename();
//
//                String filePath = fileService.uploadFileV1("album",multipartFiles.get(i));
//                if(mainIndex==i){
//                    isMain = true;
//                }
//                else {
//                    isMain = false;
//                }
//                //기존 사진 수정
//                if(i<pictures.size()){
//                    pictures.get(i).updateOriginName(originFileName);
//                    pictures.get(i).updatePathName(filePath);
//                    pictures.get(i).updateIsMain(isMain);
//                    pictureRepository.save(pictures.get(i));
//
//                }
//                else{
//                    Picture picture = Picture.builder()
//                            .album(album)
//                            .origin_name(originFileName)
//                            .path_name(filePath)
//                            .is_main(isMain)
//                            .build();
//                    pictureRepository.save(picture);
//                }
//
//            }
//        }
//        //사진 수가 기존보다 적은 경우
//        else{
//            for(int i = 0; i<pictures.size();i++){
//                if(mainIndex==i){
//                    isMain = true;
//                }
//                else {
//                    isMain = false;
//                }
//                if(i<multipartFiles.size()){
//                    String originFileName = multipartFiles.get(i).getOriginalFilename();
//                    String filePath =fileService.uploadFileV1("album",multipartFiles.get(i));
//                    pictures.get(i).updateOriginName(originFileName);
//                    pictures.get(i).updatePathName(filePath);
//                    pictures.get(i).updateIsMain(isMain);
//                    pictureRepository.save(pictures.get(i));
//                }
//                else{
//                    pictureRepository.delete(pictures.get(i));
//                }
//
//            }
//        }




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
    public void deleteAlbumReaction(long userPk, long reactionId){
        Profile profile = profileRepository.findProfileByUserPk(userPk);
        AlbumReaction albumReaction =
                albumReactionRepository.findReactionByReactionId(
                        reactionId, profile.getId());
        albumReactionRepository.delete(albumReaction);
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
                    .profileId(profile.getId())
                    .reactionId(albumReactions.get(i).getId())
                    .build();
            result.add(albumReactionListResDto);
        }
        return result;
    }

    @Transactional(readOnly = false)
    public List<Album> findAlbumsByHashTag(String keyword, long albumId){

        return albumRepository.findAlbumByHashTag(keyword,albumId);
    }

    @Transactional(readOnly = false)
    public List<Album> findAlbumsByDate(String date, long albumId){

        return albumRepository.findAlbumByDate(date,albumId);
    }



}
