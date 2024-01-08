package com.example.jwtProject.Repository;

import com.example.jwtProject.Entity.RegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRegi extends JpaRepository<RegistrationEntity, Long> {
    public Optional<RegistrationEntity> findByEmailId(String emailId);
    RegistrationEntity findByPassword(String password);

     public Optional<RegistrationEntity> findByCorporateId(Long corporateId);

    RegistrationEntity findByGstCertificateName(String fileName);
}
