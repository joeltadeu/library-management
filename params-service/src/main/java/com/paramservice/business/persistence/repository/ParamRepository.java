package com.paramservice.business.persistence.repository;

import com.paramservice.business.persistence.entity.ParamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParamRepository extends JpaRepository<ParamEntity, Long> {
    @Query("from ParamEntity where key = :key")
    Optional<ParamEntity> findByKey(String key);
}
