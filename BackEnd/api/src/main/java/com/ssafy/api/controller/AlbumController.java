package com.ssafy.api.controller;


import com.ssafy.api.config.jwt.JwtProvider;
import com.ssafy.api.dto.req.AlbumReactionReqDto;
import com.ssafy.api.dto.req.AlbumReqDto;
import com.ssafy.api.dto.req.ProfileReqDto;
import com.ssafy.api.dto.req.StatusReqDto;
import com.ssafy.api.dto.res.*;
import com.ssafy.api.entity.*;
import com.ssafy.api.service.AlbumService;
import com.ssafy.api.service.ProfileService;
import com.ssafy.api.service.UserService;
import com.ssafy.api.service.common.CommonResult;
import com.ssafy.api.service.common.ListResult;
import com.ssafy.api.service.common.ResponseService;
import com.ssafy.api.service.common.SingleResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Api(tags = {"앨범"})
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/album")
public class AlbumController {

    private final JwtProvider jwtProvider;
    private final AlbumService albumService;
    private final UserService userService;
    private final ProfileService profileService;
    private final ResponseService responseService;
//앨범수정, 앨범 검색

    @ApiImplicitParams({@ApiImplicitParam(name = "X-Auth-Token", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "앨범 전체 조회", notes = "<strong>앨범 전체 조회</strong>")
    public ListResult<AlbumResDto> getAlbums(HttpServletRequest request) {
        Long userPK = jwtProvider.getUserPkFromRequest(request);
        Family family = albumService.findFamilyByUserPK(userPK);
        List<Album> albums = albumService.findAlbumsByFamilyId(family.getId());
        List<AlbumResDto> albumList = new ArrayList<>();

        for (int i = 0 ; i<albums.size() ; i++){
            Long albumId = albums.get(i).getId();
            Picture main = albumService.findMainPictureByAlbumId(albumId);
            AlbumResDto albumResDto =AlbumResDto.builder()
                    .album(albums.get(i))
                    .mainPicture(main)
                    .build();
            albumList.add(albumResDto);
        }
        return responseService.getListResult(albumList);
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "X-Auth-Token", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @GetMapping(value = "{albumId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "앨범 상세 조회", notes = "<strong>앨범 상세 조회</strong>")
    public SingleResult<AlbumDetailResDto> getAlbum(@PathVariable long albumId, HttpServletRequest request) {
        Long userPK = jwtProvider.getUserPkFromRequest(request);
        AlbumPictureListResDto albumPictureListResDto = AlbumPictureListResDto.builder().build();
        AlbumHashTagListResDto albumHashTagListResDto = AlbumHashTagListResDto.builder().build();

        Album album = albumService.findByAlbum(albumId);
        List<AlbumReaction> albumReactions = albumService.findAlbumReactionsByAlbumId(albumId);
        List<HashTag> hashTags = albumService.findHashTagsByAlbumId(albumId);
        List<Picture> pictures = albumService.findPicturesByAlbumId(albumId);

        AlbumDetailResDto albumDetailResDto = AlbumDetailResDto.builder()
                .date(album.getDate().toString())
                .pictures(albumPictureListResDto.fromEntity(pictures))
                .hashTags(albumHashTagListResDto.fromEntity(hashTags))
                .albumReactions(albumService.findReactionList(albumReactions, userPK))
                .build();

        return responseService.getSingleResult(albumDetailResDto);
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "X-Auth-Token", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "앨범 등록", notes = "<strong>앨범 등록</strong>")
    public CommonResult createAlbum(@ModelAttribute @Valid AlbumReqDto albumReqDto,
                                    @RequestParam(value = "file", required = false) List<MultipartFile> multipartFiles,
            HttpServletRequest request){

            Long userPK = jwtProvider.getUserPkFromRequest(request);

            Family family = albumService.findFamilyByUserPK(userPK);

            Album album = Album.builder()
                    .family(family)
                    .date(albumService.createDate(albumReqDto.getDate()))
                    .build();
            albumService.createAlbum(albumReqDto, family, album, multipartFiles, request);

            return responseService.getSuccessResult();

    }



    @ApiImplicitParams({@ApiImplicitParam(name = "X-Auth-Token", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @PostMapping(value = "/{albumId}/reaction", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "앨범 리액션 등록 및 수정", notes = "<strong>앨범 리액션 등록 및 수정</strong>")
    public CommonResult createReaction(@PathVariable long albumId,
                                       @RequestBody AlbumReactionReqDto albumReactionReqDto, HttpServletRequest request){

        Long userPK = jwtProvider.getUserPkFromRequest(request);
        Album album = albumService.findByAlbum(albumId);
        albumService.manageAlbumReaction(userPK, album, albumReactionReqDto);
        return responseService.getSuccessResult();
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "X-Auth-Token", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @DeleteMapping(value = "/{albumId}/reaction", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "앨범 리액션 삭제", notes = "<strong>앨범 리액션 삭제</strong>")
    public CommonResult deleteReaction(@PathVariable long albumId,
                                       HttpServletRequest request){

        Long userPK = jwtProvider.getUserPkFromRequest(request);
        Album album = albumService.findByAlbum(albumId);
        albumService.deleteAlbumReaction(userPK,album);
        return responseService.getSuccessResult();
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "X-Auth-Token", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @PatchMapping(value = "/{albumId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "앨범 수정", notes = "<strong>앨범 수정</strong>")
    public CommonResult updateAlbum(@PathVariable long albumId,@ModelAttribute @Valid AlbumReqDto albumReqDto,
                                    @RequestParam(value = "file", required = false) List<MultipartFile> multipartFiles,
                                    HttpServletRequest request){

        Long userPK = jwtProvider.getUserPkFromRequest(request);
        Album album = albumService.findByAlbum(albumId);
        albumService.updateAlbum(userPK, album, albumReqDto, multipartFiles, request);
        return responseService.getSuccessResult();
    }

//    @ApiImplicitParams({@ApiImplicitParam(name = "X-Auth-Token", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
//    @GetMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiOperation(value = "앨범 검색", notes = "<strong>앨범 검색 </strong>")
//    public ListResult<AlbumResDto> searchAlbum(HttpServletRequest request){
//
//
//
//        return responseService.getListResult();
//    }


}
