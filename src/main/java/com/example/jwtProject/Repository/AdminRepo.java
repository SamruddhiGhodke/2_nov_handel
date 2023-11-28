package com.example.jwtProject.Repository;

import com.example.jwtProject.Entity.AdminEntity;
import com.example.jwtProject.Entity.RegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepo extends JpaRepository<AdminEntity, Long> {

    public Optional<AdminEntity> findByAdminEmailId(String adminEmailId);
   // AdminEntity findByAdminEmailId(String adminEmailId);


}
