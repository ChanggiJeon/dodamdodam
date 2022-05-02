package com.ssafy.api.controller;

import com.ssafy.core.dto.req.SuggestionReactionReqDto;
import com.ssafy.core.dto.res.MainProfileResDto;
import com.ssafy.core.dto.res.SuggestionReactionResDto;
import com.ssafy.core.dto.res.SuggestionResDto;
import com.ssafy.api.service.MainService;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.HEADER;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main")
@Tag(name = "MainController", description = "메인컨트롤러")
public class MainController {

    private final MainService mainService;
    private final ResponseService responseService;

    @Operation(summary = "가족 Profile 정보", description = "본인을 제외한 가족의 <strong>profile<strong> 정보를 받는다.",
            parameters = {
                    @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
            })
    @GetMapping(value = "profileList")
    public ListResult<MainProfileResDto> getProfileList(Authentication authentication) {

        Long userPk = Long.parseLong(authentication.getName());

        return responseService.getListResult(mainService.getProfileList(userPk));
    }


    //  의견 관련 API
    @Operation(summary = "의견 제시 생성", description = "<strong>의견 제시<strong>를 작성한다.",
            parameters = {
                    @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
            })
    @PostMapping(value = "suggestion")
    public CommonResult createSuggestion(@RequestParam String text, Authentication authentication) {

        Long userPk = Long.parseLong(authentication.getName());

        mainService.createSuggestion(text, userPk);

        return responseService.getSuccessResult("의견 제시가 정상적으로 등록되었습니다.");
    }


    @Operation(summary = "의견 제시 삭제", description = "<strong>의견 제시<strong>를 삭제한다.",
            parameters = {
                    @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
            })
    @DeleteMapping(value = "{suggestionId}")
    public CommonResult deleteSuggestion(@PathVariable Long suggestionId, Authentication authentication) {

        Long userPk = Long.parseLong(authentication.getName());

        mainService.deleteSuggestion(suggestionId, userPk);

        return responseService.getSuccessResult("의견 제시가 정상적으로 삭제되었습니다.");
    }


    @Operation(summary = "의견 리스트 목록", description = "<strong>의견 제시 리스트<strong>를 조회한다.",
            parameters = {
                    @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
            })
    @GetMapping(value = "suggestionList")
    public ListResult<SuggestionResDto> getSuggestionList(Authentication authentication) {
        Long userPk = Long.parseLong(authentication.getName());
        return responseService.getListResult(mainService.getSuggestionList(userPk));
    }


    @Operation(summary = "의견 리액션", description = "<strong>의견 제시 리액션<strong>을 등록 혹은 수정한다.",
            parameters = {
                    @Parameter(name = "X-Auth-Token", description = "JWT Token", required = true, in = HEADER)
            })
    @PostMapping(value = "suggestion/reaction", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SingleResult<SuggestionReactionResDto> manageSuggestionReaction(
            @RequestBody SuggestionReactionReqDto request,
            Authentication authentication
    ) {

        return responseService.getSingleResult(
                mainService.manageSuggestionReaction(request, Long.parseLong(authentication.getName()))
        );
    }
}
