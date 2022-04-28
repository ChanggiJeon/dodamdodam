package com.ssafy.api.dto.req;


import com.ssafy.api.entity.HashTag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlbumReqDto {


    @NotBlank
    @Schema(description = "해시태그", required = true, example = "아들")
    private List<String> hashTags;

    @NotBlank
    @Size(max = 10, min = 1)
    @Schema(description = "앨범날짜", required = true, example = "2022-04-20")
    private String date;


//    @NotEmpty
//    @Schema(value = "앨범 사진", required = true, example = "")
//    private List<MultipartFile> multipartFiles;

    @NotNull
    @Schema(name = "메인사진 index", required = true, example = "")
    private int mainIndex;


}
