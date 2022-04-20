package com.ssafy.api.repository;

import com.ssafy.api.entity.Family;
import com.ssafy.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyRepository extends JpaRepository<Family, Long> {

    Family findByCode(String code);

    Family findById(long id);
}
