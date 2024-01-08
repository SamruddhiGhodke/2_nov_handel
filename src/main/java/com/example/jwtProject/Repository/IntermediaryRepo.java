package com.example.jwtProject.Repository;

import com.example.jwtProject.Entity.IntermediaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IntermediaryRepo extends JpaRepository<IntermediaryEntity, Long> {

    public Optional<IntermediaryEntity> findByEmailId(String emailId);

    Optional<IntermediaryEntity> findByIntermediaryId(Long intermediaryId);
}
