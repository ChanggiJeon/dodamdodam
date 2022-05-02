package com.ssafy.core.repository;

import com.ssafy.core.entity.User;
import com.ssafy.core.repository.querydsl.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    User getByUserId(String userId);

    Optional<User> findUserByUserId(String userId);

    Optional<User> findUserByUserPk(Long userPk);

    Optional<User> findUserByRefreshToken(String refreshToken);
}
