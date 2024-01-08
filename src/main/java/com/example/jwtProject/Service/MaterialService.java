package com.example.jwtProject.Service;

import com.example.jwtProject.Entity.DomesticMaterialEntity;
import com.example.jwtProject.Entity.InternationalMaterialEntity;
import com.example.jwtProject.Entity.RegistrationEntity;
import com.example.jwtProject.Model.DomesticModel;
import com.example.jwtProject.Model.InternationalModel;
import com.example.jwtProject.Repository.ClientRegi;
import com.example.jwtProject.Repository.DomesticRepo;
import com.example.jwtProject.Repository.InternationalRepo;
import com.example.jwtProject.Util.EmailUtil;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class MaterialService {
    @Autowired
    public DomesticRepo domesticRepo;

    @Autowired
    public InternationalRepo internationalRepo;

    @Autowired
    public ClientRegi clientRegi;


    @Autowired
    private EmailUtil emailUtil;



    // API for creating or updating a domestic entry
    public DomesticMaterialEntity createDomestic(DomesticModel domesticModel, Long corporateId) throws Exception, MessagingException {
        System.out.println(corporateId);
        Long domesticMaterialId = domesticModel.getDomesticMaterialId();

        if (domesticMaterialId != null && domesticMaterialId != 0) {
            return updateDomesticMaterial(domesticModel);
        } else {
            if (corporateId != null) {
                RegistrationEntity registrationEntity = clientRegi.findByCorporateId(corporateId)
                        .orElseThrow(() -> new Exception("Invalid corporateId: " + corporateId));
                return createNewDomesticMaterial(domesticModel, registrationEntity);
            } else {
                throw new Exception("Invalid corporateId: corporateId is null");
            }
        }
    }

    private DomesticMaterialEntity updateDomesticMaterial(DomesticModel domesticModel) {
        Long domesticMaterialId = domesticModel.getDomesticMaterialId();
        DomesticMaterialEntity domesticMaterialEntity = domesticRepo.findByDomesticMaterialId(domesticMaterialId);
        setDomesticMaterialProperties(domesticMaterialEntity, domesticModel);
        return domesticRepo.save(domesticMaterialEntity);
    }

    private DomesticMaterialEntity createNewDomesticMaterial(DomesticModel domesticModel, RegistrationEntity registrationEntity) throws MessagingException {
        DomesticMaterialEntity domesticMaterial = new DomesticMaterialEntity();
        setDomesticMaterialProperties(domesticMaterial, domesticModel);
        domesticMaterial.setRegistration(registrationEntity);
        domesticRepo.save(domesticMaterial);

        // Update the RegistrationEntity with the DomesticMaterialId
        registrationEntity.setDomesticMaterialId(domesticMaterial.getDomesticMaterialId());
        clientRegi.save(registrationEntity);

        // Send an email notification
        emailUtil.domesticEmail(domesticModel.getJwtModel().getEmailId());
        System.out.println("Email Id: " + domesticModel.getJwtModel().getEmailId());

        return domesticMaterial;
    }

    private void setDomesticMaterialProperties(DomesticMaterialEntity domesticMaterialEntity, DomesticModel domesticModel) {
        domesticMaterialEntity.setMaterialName(domesticModel.getMaterialName());
        domesticMaterialEntity.setQuantity(domesticModel.getQuantity());
        domesticMaterialEntity.setCreditPeriod(domesticModel.getCreditPeriod());
        domesticMaterialEntity.setUnitPrice(domesticModel.getUnitPrice());
        domesticMaterialEntity.setSupplierName(domesticModel.getSupplierName());
    }

    // API for creating or updating a international entry
        public InternationalMaterialEntity createInternational(InternationalModel internationalModel, Long corporateId) throws Exception {
        System.out.println(corporateId);
        Long internationalMaterialId = internationalModel.getInternationalMaterialId();

        if (internationalMaterialId != null && internationalMaterialId != 0) {
            InternationalMaterialEntity internationalMaterialEntity = internationalRepo.findByInternationalMaterialId(internationalMaterialId);
            internationalMaterialEntity.setMaterialName(internationalModel.getMaterialName());
            internationalMaterialEntity.setQuantity(internationalModel.getQuantity());
            internationalMaterialEntity.setCreditPeriod(internationalModel.getCreditPeriod());
            internationalMaterialEntity.setUnitPrice(internationalModel.getUnitPrice());
            internationalMaterialEntity.setSupplierName(internationalModel.getSupplierName());

            return internationalRepo.save(internationalMaterialEntity);
        } else {
            if (corporateId != null) {
                Optional<RegistrationEntity> registrationEntityOptional = clientRegi.findByCorporateId(corporateId);
                if (registrationEntityOptional.isPresent()) {
                    RegistrationEntity registrationEntity = registrationEntityOptional.get();
                    InternationalMaterialEntity internationalMaterial = new InternationalMaterialEntity();
                    internationalMaterial.setMaterialName(internationalModel.getMaterialName());
                    internationalMaterial.setQuantity(internationalModel.getQuantity());
                    internationalMaterial.setCreditPeriod(internationalModel.getCreditPeriod());
                    internationalMaterial.setUnitPrice(internationalModel.getUnitPrice());
                    internationalMaterial.setSupplierName(internationalModel.getSupplierName());
                    internationalRepo.save(internationalMaterial);
                    registrationEntity.setInternationalMaterialId(internationalMaterial.getInternationalMaterialId());
                    clientRegi.save(registrationEntity);
                    emailUtil.InternationalEmail(internationalModel.getJwtModel().getEmailId());
                    return internationalMaterial;
                }
            } else {
                throw new Exception("Invalid corporateId: corporateId is null");
            }
        }
        return null;
    }
    }

