package com.ssafy.api.controller;


import com.ssafy.api.config.jwt.JwtProvider;
import com.ssafy.api.dto.req.AlbumReqDto;
import com.ssafy.api.entity.Album;
import com.ssafy.api.entity.Profile;
import com.ssafy.api.service.AlbumService;
import com.ssafy.api.service.ProfileService;
import com.ssafy.api.service.UserService;
import com.ssafy.api.service.common.CommonResult;
import com.ssafy.api.service.common.ListResult;
import com.ssafy.api.service.common.ResponseService;
import com.ssafy.api.service.common.SingleResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;

@Tag(name = "앨범")
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


    @Parameters({@Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)})
    @GetMapping("")
    @Operation(summary  = "앨범 전체 조회", description  = "<strong>앨범 전체 조회</strong>")
    public ListResult<Album> getAlbums(HttpServletRequest request) {
        String token = jwtProvider.resolveToken(request);
        Long userPk = jwtProvider.getUserPk(token);
        Profile profile = profileService.findProfileByUserPk(userPk);
        List<Album> albums = albumService.findAlbumsByFamilyId(profile.getFamily().getId());

        return responseService.getListResult(albums);
    }

    @Parameters({@Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)})
    @GetMapping(value = "{albumId}")
    @Operation(summary = "앨범 상세 조회", description = "<strong>앨범 상세 조회</strong>")
    public SingleResult<Album> getAlbum(@PathVariable long albumId, HttpServletRequest request) {

        Album album = albumService.findByAlbum(albumId);

        return responseService.getSingleResult(album);
    }

    @Parameters({@Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)})
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "앨범 등록", description = "<strong>앨범 등록</strong>")
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
