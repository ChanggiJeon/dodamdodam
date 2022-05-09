package com.ssafy.core.dto.req;

import com.ssafy.core.validation.Password;
import com.ssafy.core.validation.UserId;
import com.ssafy.core.validation.UserIdUnique;
import com.ssafy.core.validation.UserName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO Model : SignUpReqDto")
public class SignUpReqDto {

    @UserId
    @UserIdUnique
    @Schema(description = "아이디", required = true, example = "ssafy")
    private String userId;

    @Password
    @Schema(description = "비밀번호", required = true, example = "ssafy61!")
    private String password;

    @UserName
    @Schema(description = "이름", required = true, example = "싸피")
    private String name;

}