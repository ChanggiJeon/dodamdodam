package com.ssafy.api.repository;

import com.ssafy.api.entity.User;
import com.ssafy.api.repository.querydsl.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    User getByUserId(String userId);

    Optional<User> findUserByUserId(String userId);

    Optional<User> findUserByRefreshToken(String refreshToken);
}
