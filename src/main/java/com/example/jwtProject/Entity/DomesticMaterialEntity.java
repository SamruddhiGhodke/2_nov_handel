package com.example.jwtProject.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name="Domestic_Material")
public class DomesticMaterialEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="domestic_Material_Id")
    private Long domesticMaterialId;

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
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "corporate_id")
    private RegistrationEntity registration;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "trader_Id")
    private TradersEntity tradersEntity;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "Intermediary_Id")
    private IntermediaryEntity intermediaryEntity;

    public DomesticMaterialEntity() {
    }

    public Long getDomesticMaterialId() {
        return domesticMaterialId;
    }

    public void setDomesticMaterialId(Long domesticMaterialId) {
        this.domesticMaterialId = domesticMaterialId;
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

    public RegistrationEntity getRegistration() {
        return registration;
    }

    public void setRegistration(RegistrationEntity registration) {
        this.registration = registration;
    }

    public TradersEntity getTradersEntity() {
        return tradersEntity;
    }

    public void setTradersEntity(TradersEntity tradersEntity) {
        this.tradersEntity = tradersEntity;
    }

    public IntermediaryEntity getIntermediaryEntity() {
        return intermediaryEntity;
    }

    public void setIntermediaryEntity(IntermediaryEntity intermediaryEntity) {
        this.intermediaryEntity = intermediaryEntity;
    }
}
