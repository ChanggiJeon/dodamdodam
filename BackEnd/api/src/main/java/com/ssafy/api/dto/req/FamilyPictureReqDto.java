package com.ssafy.api.dto.req;


import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "FamilyPicture Request")
public class FamilyPictureReqDto {
    @NotNull
    private String picture;
}
