package com.example.jwtProject.Repository;

import com.example.jwtProject.Entity.InternationalMaterialEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InternationalRepo extends JpaRepository<InternationalMaterialEntity, Long> {
    InternationalMaterialEntity findByInternationalMaterialId(Long internationalId);
}
