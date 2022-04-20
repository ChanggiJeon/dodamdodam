package com.ssafy.api.controller;

import com.ssafy.api.dto.res.FamilyCodeResDto;
import com.ssafy.api.entity.Family;
import com.ssafy.api.service.FamilyService;
import com.ssafy.api.service.common.CommonResult;
import com.ssafy.api.service.common.ResponseService;
import com.ssafy.api.service.common.SingleResult;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/family")
@RequiredArgsConstructor
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
}
