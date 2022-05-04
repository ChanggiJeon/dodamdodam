package com.ssafy.core.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AlarmRepoCustomImpl implements AlarmRepoCustom{

    private final JPAQueryFactory jpaQueryFactory;
}
