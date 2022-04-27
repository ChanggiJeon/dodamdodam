package com.ssafy.api.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "FcmToken Request")
public class FcmTokenReqDto {
    @NotNull
    @Schema(name = "fcm토큰", required = true, example = "c1~~")
    private String fcmToken;
}
