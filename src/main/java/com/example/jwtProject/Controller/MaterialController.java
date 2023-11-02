package com.example.jwtProject.Controller;

import com.example.jwtProject.Entity.DomesticMaterialEntity;
import com.example.jwtProject.Entity.InternationalMaterialEntity;
import com.example.jwtProject.Model.DomesticModel;
import com.example.jwtProject.Model.InternationalModel;
import com.example.jwtProject.Service.MaterialService;
import com.sun.jdi.InternalException;
import jakarta.mail.MessagingException;
import jdk.jshell.spi.ExecutionControl;
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

    // API for creating a domestic material entity
//    @PostMapping("/domesticMaterial")
//    public DomesticMaterialEntity createDomestic(@RequestBody DomesticModel domesticModel, Long corporateId) throws Exception {
//        return materialService.createDomestic(domesticModel, corporateId);
//    }

//    @PostMapping("/domesticMaterial")
//    public DomesticMaterialEntity createDomestic(@RequestBody DomesticModel domesticModel) throws MessagingException, Exception {
//        Long corporateId = domesticModel.getJwtModel().getCorporateId();
//        if (corporateId == null) {
//            throw new Exception("Invalid corporateId: corporateId is null");
//        }
//        return materialService.createDomestic(domesticModel, corporateId);
//    }

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
            // Handle the exception appropriately, e.g., return an error response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }



    // API for creating an international material entity
    @PostMapping("/internationalMaterial")
    public InternationalMaterialEntity createInternational(@RequestBody InternationalModel internationalModel){
        return materialService.createInternational(internationalModel);
    }
}
