package com.ssafy.api.controller;

import com.ssafy.api.dto.res.FamilyCodeResDto;
import com.ssafy.api.entity.Family;
import com.ssafy.api.service.FamilyService;
import com.ssafy.api.service.common.CommonResult;
import com.ssafy.api.service.common.ResponseService;
import com.ssafy.api.service.common.SingleResult;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/family")
@RequiredArgsConstructor
@Slf4j
public class FamilyController {

    private final FamilyService familyService;
    private final ResponseService responseService;

    @PostMapping("/create")
    @ApiOperation(value = "가족 그룹 생성")
    public CommonResult createFamily() {
        familyService.createFamily();
        return responseService.getSuccessResult("그룹 생성 완료");
    }

    @GetMapping("/code/{familyId}")
    @ApiOperation(value = "가족 코드 조회", notes = "<strong>가족 id<strong>를 받아 가족 코드를 조회한다.")
    public SingleResult<FamilyCodeResDto> getFamilyCode (@PathVariable long familyId){
        Family family = familyService.getFamilyCode(familyId);
        FamilyCodeResDto res = FamilyCodeResDto.builder()
                .code(family.getCode())
                .build();
        return responseService.getSingleResult(res);
    }

    @PutMapping("/picture")
    @ApiOperation(value = "가족 사진 수정", notes = "<strong>가족 사진<stong>을 추가 및 수정한다.")
    public CommonResult updateFamilyPicture(
            @RequestParam(value="picture", required=true) MultipartFile picture, HttpServletRequest request) {
        String path = request.getServletContext().getRealPath("/resources/upload/familyPicture");
        log.info(path);
        log.info("file name:" + picture.getOriginalFilename());
        familyService.updateFamilyPicture(picture, path);
        return responseService.getSuccessResult();
    }
}
