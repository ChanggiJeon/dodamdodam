package com.ssafy.api.repository;

import com.ssafy.api.entity.Family;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyRepository extends JpaRepository<Family, Long>{

    Family findFamilyByCode(String code);

    Family findFamilyById(long id);
}
