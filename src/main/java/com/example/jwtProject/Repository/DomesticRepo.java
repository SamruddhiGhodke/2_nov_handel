package com.example.jwtProject.Repository;

import com.example.jwtProject.Entity.DomesticMaterialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomesticRepo extends JpaRepository<DomesticMaterialEntity, Long> {
    DomesticMaterialEntity findByDomesticMaterialId(Long domesticId);
}
