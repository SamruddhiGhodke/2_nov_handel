package com.example.jwtProject.Model;

public class DomesticModel {
    private Long domesticMaterialId;
    private String materialName;
    private String supplierName;
    private String creditPeriod;
    private String quantity;
    private String unitPrice;
    private Long corporateId;
    private Long traderId;
    private Long intermediaryId;
    private String emailId;

    //private JwtModel jwtModel;


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

//    public JwtModel getJwtModel() {
//        return jwtModel;
//    }
//
//    public void setJwtModel(JwtModel jwtModel) {
//        this.jwtModel = jwtModel;
//    }

    public Long getCorporateId() {
        return corporateId;
    }

    public void setCorporateId(Long corporateId) {
        this.corporateId = corporateId;
    }

    public Long getTraderId() {
        return traderId;
    }

    public void setTraderId(Long traderId) {
        this.traderId = traderId;
    }

    public Long getIntermediaryId() {
        return intermediaryId;
    }

    public void setIntermediaryId(Long intermediaryId) {
        this.intermediaryId = intermediaryId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
