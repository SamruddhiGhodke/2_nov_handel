package com.example.jwtProject.Model;

public class InternationalModel {
    private Long internationalMaterialId;
    private String materialName;
    private String supplierName;
    private String creditPeriod;
    private String quantity;
    private String unitPrice;

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
}
