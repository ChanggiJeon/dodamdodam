package com.ssafy.core.dto.req;

import com.ssafy.core.validation.Password;
import com.ssafy.core.validation.UserId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO Model : SignInReqDto")
public class SignInReqDto {

    @UserId
    @Schema(description = "아이디", required = true, example = "ssafy")
    private String userId;

    @Password
    @Schema(description = "비밀번호", required = true, example = "ssafy61!")
    private String password;

}