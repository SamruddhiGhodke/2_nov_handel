package com.example.jwtProject.Entity;

import jakarta.persistence.*;

@Entity
@Table(name="International_Material")
public class InternationalMaterialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="International_Material_Id")
    private Long internationalMaterialId;



    @Column(name="Material_Name")
    private String materialName;

    @Column(name="Supplier_Name")
    private String supplierName;

    @Column(name="Credit_Period")
    private String creditPeriod;

    @Column(name="Quantity")
    private String quantity;

    @Column(name="Unit_Price")
    private String unitPrice;

    @ManyToOne
    @JoinColumn(name = "corporate_id")
    private RegistrationEntity registrationEntity;

//    @ManyToOne
//    private RegistrationEntity registrationEntity;

    public InternationalMaterialEntity() {
    }

    public Long getInternationalMaterialId() {
        return internationalMaterialId;
    }

    public void setInternationalMaterialId(Long internationalMaterialId) {
        this.internationalMaterialId = internationalMaterialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getCreditPeriod() {
        return creditPeriod;
    }

    public void setCreditPeriod(String creditPeriod) {
        this.creditPeriod = creditPeriod;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public RegistrationEntity getRegistrationEntity() {
        return registrationEntity;
    }

    public void setRegistrationEntity(RegistrationEntity registrationEntity) {
        this.registrationEntity = registrationEntity;
    }
//    public RegistrationEntity getRegistrationEntity() {
//        return registrationEntity;
//    }
//
//    public void setRegistrationEntity(RegistrationEntity registrationEntity) {
//        this.registrationEntity = registrationEntity;
//    }
}
