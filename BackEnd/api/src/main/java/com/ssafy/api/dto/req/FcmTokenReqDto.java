package com.ssafy.api.dto.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "FcmToken Request")
public class FcmTokenReqDto {
    @NotNull
    @ApiModelProperty(value = "fcm토큰", required = true, example = "c1~~")
    private String fcmToken;
}
