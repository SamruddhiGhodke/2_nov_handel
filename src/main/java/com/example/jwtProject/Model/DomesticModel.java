package com.example.jwtProject.Model;

public class DomesticModel {
    private Long domesticMaterialId;
    private String materialName;
    private String supplierName;
    private String creditPeriod;
    private String quantity;
    private String unitPrice;

    private JwtModel jwtModel;


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

    public JwtModel getJwtModel() {
        return jwtModel;
    }

    public void setJwtModel(JwtModel jwtModel) {
        this.jwtModel = jwtModel;
    }
}
