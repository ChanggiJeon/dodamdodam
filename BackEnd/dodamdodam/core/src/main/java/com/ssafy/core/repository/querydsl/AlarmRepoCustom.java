package com.ssafy.core.repository.querydsl;

import com.ssafy.core.entity.Alarm;
import com.ssafy.core.entity.Profile;

public interface AlarmRepoCustom {
    Alarm findAlarmByProfileAndTarget(Profile me, Profile target);
}
