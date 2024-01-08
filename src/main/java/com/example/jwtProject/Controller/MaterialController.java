package com.example.jwtProject.Controller;

import com.example.jwtProject.Entity.DomesticMaterialEntity;
import com.example.jwtProject.Entity.InternationalMaterialEntity;
import com.example.jwtProject.Model.DomesticModel;
import com.example.jwtProject.Model.InternationalModel;
import com.example.jwtProject.Service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/material")
public class MaterialController {
    @Autowired
    public MaterialService materialService;


    @PostMapping("/domesticMaterial")
    public ResponseEntity<DomesticMaterialEntity> createDomestic(@RequestBody DomesticModel domesticModel) {
        try {
            Long corporateId = domesticModel.getJwtModel().getCorporateId();
            if (corporateId == null) {
                throw new Exception("Invalid corporateId: corporateId is null");
            }
            DomesticMaterialEntity createdDomesticMaterial = materialService.createDomestic(domesticModel, corporateId);
            return ResponseEntity.ok(createdDomesticMaterial);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }


    @PostMapping("/internationalMaterial")
    public ResponseEntity<InternationalMaterialEntity> createInternational(@RequestBody InternationalModel internationalModel) {
        try {
            Long corporateId = internationalModel.getJwtModel().getCorporateId();
            if (corporateId == null) {
                throw new Exception("Invalid corporateId: corporateId is null");
            }
            InternationalMaterialEntity createdInternationalMaterial = materialService.createInternational(internationalModel, corporateId);
            return ResponseEntity.ok(createdInternationalMaterial);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
