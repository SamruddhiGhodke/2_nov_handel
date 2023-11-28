package com.example.jwtProject.Repository;

import com.example.jwtProject.Entity.RequestSupportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RequestSupportRepo extends JpaRepository<RequestSupportEntity, Long> {
    Optional<Object> findByUserEmail(String emailId);
}
