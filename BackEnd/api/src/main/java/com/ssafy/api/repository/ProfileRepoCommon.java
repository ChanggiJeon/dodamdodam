package com.ssafy.api.repository;

import com.ssafy.api.entity.Profile;

public interface ProfileRepoCommon {
    Profile findProfileById(String userId);
}
