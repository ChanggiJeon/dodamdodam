package com.ssafy.api.controller;


import com.ssafy.api.config.jwt.JwtProvider;
import com.ssafy.api.dto.req.AlbumReqDto;
import com.ssafy.api.dto.req.ProfileReqDto;
import com.ssafy.api.dto.req.StatusReqDto;
import com.ssafy.api.entity.Album;
import com.ssafy.api.entity.Profile;
import com.ssafy.api.entity.User;
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


    @ApiImplicitParams({@ApiImplicitParam(name = "X-Auth-Token", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @GetMapping("")
    @ApiOperation(value = "앨범 전체 조회", notes = "<strong>앨범 전체 조회</strong>")
    public ListResult<Album> getAlbums(HttpServletRequest request) {
        String token = jwtProvider.resolveToken(request);
        Long userPk = jwtProvider.getUserPk(token);
        Profile profile = profileService.findProfileByUserPk(userPk);
        List<Album> albums = albumService.findAlbumsByFamilyId(profile.getFamily().getId());

        return responseService.getListResult(albums);
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "X-Auth-Token", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @GetMapping(value = "{albumId}")
    @ApiOperation(value = "앨범 상세 조회", notes = "<strong>앨범 상세 조회</strong>")
    public SingleResult<Album> getAlbum(@PathVariable long albumId, HttpServletRequest request) {

        Album album = albumService.findByAlbum(albumId);

        return responseService.getSingleResult(album);
    }

    @ApiImplicitParams({@ApiImplicitParam(name = "X-Auth-Token", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "앨범 등록", notes = "<strong>앨범 등록</strong>")
    public CommonResult createAlbum(@ModelAttribute @Valid AlbumReqDto albumReqDto,
            @RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles, HttpServletRequest request){
        String token = jwtProvider.resolveToken(request);
        Long userPk = jwtProvider.getUserPk(token);
        Profile profile = profileService.findProfileByUserPk(userPk);
        String familyCode = profile.getFamily().getCode();
        String[] pictures = albumService.createAlbum(multipartFiles,familyCode,request);

        Album album = Album.builder()
                .family(profile.getFamily())
                .date(albumService.createDate(albumReqDto.getDate()))
                .build();
        return responseService.getSuccessResult();
    }





}
