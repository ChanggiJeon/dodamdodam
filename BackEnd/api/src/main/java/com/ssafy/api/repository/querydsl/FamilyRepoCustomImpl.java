package com.ssafy.api.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FamilyRepoCustomImpl implements FamilyRepoCustom {

    private final JPAQueryFactory jpaQueryFactory;

}
