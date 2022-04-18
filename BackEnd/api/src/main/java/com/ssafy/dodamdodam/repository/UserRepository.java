package com.ssafy.dodamdodam.repository;

import com.ssafy.dodamdodam.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User getByUserId(String userId);
}
