package com.ssafy.api.dto.res;

import io.swagger.annotations.ApiModel;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "FamilyPicture Response")
public class FamilyPictureResDto {
    private String picture;
}
