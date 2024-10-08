package com.example.jwtProject.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
@Table(name = "Intermediary")
public class IntermediaryEntity  implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Intermediary_Id")
    private Long intermediaryId;

    @Column(name="domestic_Material_Id")
    private Long domesticMaterialId;

    @Column(name="International_Material_Id")
    private Long internationalMaterialId;

    @Column(name="entityName")
    private String entityName;
    @Column(name="iecCode")
    private String iecCode;
    @Column(name="gstCertificates")
    private String gstCertificates;
    @Column(name="financialCertificates")
    private String financialCertificates;
    @Column(name="userName")
    private String userName;
    @Column(name="mobileNumber")
    private Long mobileNumber;
    @Column(name="emailId")
    private String emailId;
    @Column(name="beneficiary")
    private String beneficiary;
    @Column(name="accountNumber")
    private String accountNumber;
    @Column(name="swiftCode")
    private String swiftCode;

    @Column(name="Password")
    private String password;

    @Column(name="gstCertificateName")
    private String gstCertificateName;

    @Column(name="financialName")
    private String financialName;

    @Column(name="Admin_Id")
    private Long adminId;

    @Column(name="status")
    private String status;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "Admin_Id",insertable=false, updatable=false)
    private AdminEntity adminEntity;


    public IntermediaryEntity() {
    }

    public Long getIntermediaryId() {
        return intermediaryId;
    }

    public void setIntermediaryId(Long intermediaryId) {
        this.intermediaryId = intermediaryId;
    }

    public Long getDomesticMaterialId() {
        return domesticMaterialId;
    }

    public void setDomesticMaterialId(Long domesticMaterialId) {
        this.domesticMaterialId = domesticMaterialId;
    }

    public Long getInternationalMaterialId() {
        return internationalMaterialId;
    }

    public void setInternationalMaterialId(Long internationalMaterialId) {
        this.internationalMaterialId = internationalMaterialId;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getIecCode() {
        return iecCode;
    }

    public void setIecCode(String iecCode) {
        this.iecCode = iecCode;
    }

    public String getGstCertificates() {
        return gstCertificates;
    }

    public void setGstCertificates(String gstCertificates) {
        this.gstCertificates = gstCertificates;
    }

    public String getFinancialCertificates() {
        return financialCertificates;
    }

    public void setFinancialCertificates(String financialCertificates) {
        this.financialCertificates = financialCertificates;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(Long mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return this.emailId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGstCertificateName() {
        return gstCertificateName;
    }

    public void setGstCertificateName(String gstCertificateName) {
        this.gstCertificateName = gstCertificateName;
    }

    public String getFinancialName() {
        return financialName;
    }

    public void setFinancialName(String financialName) {
        this.financialName = financialName;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public AdminEntity getAdminEntity() {
        return adminEntity;
    }

    public void setAdminEntity(AdminEntity adminEntity) {
        this.adminEntity = adminEntity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
