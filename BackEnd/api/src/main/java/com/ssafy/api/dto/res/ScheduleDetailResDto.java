package com.ssafy.api.dto.res;

import io.swagger.annotations.ApiModel;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "ScheduleDetail Response")
public class ScheduleDetailResDto {
    private long id;
    private String title;
    private String content;
    private String startDate;
    private String endDate;
    private String role;
}
