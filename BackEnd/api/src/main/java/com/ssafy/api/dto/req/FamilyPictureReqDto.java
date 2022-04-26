package com.ssafy.api.dto.req;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "FamilyPicture Request")
public class FamilyPictureReqDto {
    @NotNull
    private String picture;
}
