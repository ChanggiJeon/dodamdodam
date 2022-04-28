package com.ssafy.api.controller;


import com.ssafy.api.config.jwt.JwtProvider;
import com.ssafy.api.dto.req.AlbumReactionReqDto;
import com.ssafy.api.dto.req.AlbumReqDto;
import com.ssafy.api.entity.Album;
import com.ssafy.api.dto.res.*;
import com.ssafy.api.entity.*;
import com.ssafy.api.service.AlbumService;
import com.ssafy.api.service.common.CommonResult;
import com.ssafy.api.service.common.ListResult;
import com.ssafy.api.service.common.ResponseService;
import com.ssafy.api.service.common.SingleResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/album")
@Tag(name = "AlbumController", description = "앨범컨트롤러")
public class AlbumController {

    private final JwtProvider jwtProvider;
    private final AlbumService albumService;
    private final ResponseService responseService;

    //앨범수정, 앨범 검색
    @Operation(summary = "앨범 전체 조회", description = "<strong>앨범 전체 조회</strong>",
            parameters = {
                    @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
            })
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ListResult<AlbumResDto> getAlbums(HttpServletRequest request) {
        Long userPK = jwtProvider.getUserPkFromRequest(request);
        Family family = albumService.findFamilyByUserPK(userPK);
        List<Album> albums = albumService.findAlbumsByFamilyId(family.getId());
        List<AlbumResDto> albumList = new ArrayList<>();

        for (int i = 0; i < albums.size(); i++) {
            Long albumId = albums.get(i).getId();
            Picture main = albumService.findMainPictureByAlbumId(albumId);
            AlbumResDto albumResDto = AlbumResDto.builder()
                    .album(albums.get(i))
                    .mainPicture(main)
                    .build();
            albumList.add(albumResDto);
        }
        return responseService.getListResult(albumList);
    }


    @Operation(summary = "앨범 상세 조회", description = "<strong>앨범 상세 조회</strong>",
            parameters = {
                    @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
            })
    @GetMapping(value = "{albumId}")
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


    @Operation(summary = "앨범 등록", description = "<strong>앨범 등록</strong>", parameters = {
            @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
    })
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResult createAlbum(@org.springframework.web.bind.annotation.RequestBody
                                    @io.swagger.v3.oas.annotations.parameters.RequestBody
                                    @Valid final AlbumReqDto albumReqDto,
                                    @RequestParam(value = "file", required = false) List<MultipartFile> multipartFiles,
                                    HttpServletRequest request) {

        Long userPK = jwtProvider.getUserPkFromRequest(request);

        Family family = albumService.findFamilyByUserPK(userPK);

        Album album = Album.builder()
                .family(family)
                .date(albumService.createDate(albumReqDto.getDate()))
                .build();
        albumService.createAlbum(albumReqDto, family, album, multipartFiles, request);

        return responseService.getSuccessResult();

    }


    @Operation(summary = "앨범 리액션 등록 및 수정", description = "<strong>앨범 리액션 등록 및 수정</strong>",
            parameters = {
                    @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
            })
    @PostMapping(value = "/{albumId}/reaction", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResult createReaction(@PathVariable long albumId,
                                       @org.springframework.web.bind.annotation.RequestBody
                                       @io.swagger.v3.oas.annotations.parameters.RequestBody
                                               AlbumReactionReqDto albumReactionReqDto,
                                       HttpServletRequest request) {

        Long userPK = jwtProvider.getUserPkFromRequest(request);
        Album album = albumService.findByAlbum(albumId);
        albumService.manageAlbumReaction(userPK, album, albumReactionReqDto);
        return responseService.getSuccessResult();
    }


    @Operation(summary = "앨범 리액션 삭제", description = "<strong>앨범 리액션 삭제</strong>",
            parameters = {
                    @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
            })
    @DeleteMapping(value = "/{albumId}/reaction", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResult deleteReaction(@PathVariable long albumId, HttpServletRequest request) {

        Long userPK = jwtProvider.getUserPkFromRequest(request);
        Album album = albumService.findByAlbum(albumId);
        albumService.deleteAlbumReaction(userPK, album);
        return responseService.getSuccessResult();
    }

    @Operation(summary = "앨범 수정", description = "<strong>앨범 수정</strong>",
            parameters = {
                    @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
            })
    @PatchMapping(value = "/{albumId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResult updateAlbum(@PathVariable long albumId,
                                    @org.springframework.web.bind.annotation.RequestBody
                                    @io.swagger.v3.oas.annotations.parameters.RequestBody
                                    @Valid AlbumReqDto albumReqDto,
                                    @RequestParam(value = "file", required = false) List<MultipartFile> multipartFiles,
                                    HttpServletRequest request) {

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
