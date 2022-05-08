package com.ssafy.family.util

object Constants {
    const val TAG : String = "로그"
}

enum class UiMode {
    PROGRESS,
    READY,
    FAIL
}

enum class Status {
    SUCCESS,
    ERROR,
    LOADING,
    EXPIRED
}