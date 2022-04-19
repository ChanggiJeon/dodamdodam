package com.ssafy.api.repository;

import com.ssafy.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User getByUserId(String userId);
}
