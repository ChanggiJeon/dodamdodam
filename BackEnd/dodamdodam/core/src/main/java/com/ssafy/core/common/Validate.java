package com.ssafy.core.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Validate {

    USER_ID_MAX(20),
    USER_ID_MIN(4),
    USER_PWD_MAX(20),
    USER_PWD_MIN(8),
    USER_NAME_MIN(1),
    USER_NAME_MAX(10);

    private final int number;
}
