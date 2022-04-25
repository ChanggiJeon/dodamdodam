package com.ssafy.api.service;


import com.ssafy.api.entity.Album;
import com.ssafy.api.exception.CustomException;
import com.ssafy.api.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static com.ssafy.api.exception.CustomErrorCode.INVALID_REQUEST;

@Service
@Slf4j
@RequiredArgsConstructor
public class AlbumService {
    private AlbumRepository albumRepository;

    public List<Album> findAlbumsByFamilyId(long familyId){
        return albumRepository.findAlbumByFamilyId(familyId);
    }

    public Album findByAlbum(long albumId){
        return albumRepository.findAlbumByAlbumId(albumId);
    }
    public LocalDate createDate(String date){
        String[] list = date.split("-");
        LocalDate updateDate = LocalDate.of(Integer.parseInt(list[0]), Integer.parseInt(list[1]), Integer.parseInt(list[2]));
        return updateDate;
    }

    public String[] createAlbum(List<MultipartFile> multipartFiles,String familyCode, HttpServletRequest request) {
        String[] result =  new String[multipartFiles.size()];
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

            for(int i = 0 ; i<multipartFiles.size() ; i++){
                originFileNames[i] = multipartFiles.get(i).getOriginalFilename();
                saveFileName = UUID.randomUUID().toString() + originFileNames[i].substring(originFileNames[i].lastIndexOf("."));
                filePath = savePath+separ+saveFileName;
                multipartFiles.get(i).transferTo(new File(filePath));
                realPaths[i] = savePath+saveFileName;
                result[i] = realPaths[i]+"#"+originFileNames[i];
            }
//            String originFileName = multipartFile.getOriginalFilename();
//            String saveFileName = UUID.randomUUID().toString() + originFileName.substring(originFileName.lastIndexOf("."));
//
//            String filePath = savePath+separ+saveFileName;
//            multipartFile.transferTo(new File(filePath));
//            String realPath = savePath+saveFileName;
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;

    }




}
