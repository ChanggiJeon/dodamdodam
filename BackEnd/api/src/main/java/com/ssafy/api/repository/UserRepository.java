package com.ssafy.api.repository;

import com.ssafy.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User getByUserId(String userId);
    Optional<User> findUserByUserId(String userId);
}
