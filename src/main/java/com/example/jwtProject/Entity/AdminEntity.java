package com.example.jwtProject.Entity;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name="Admin")
public class AdminEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Admin_Id")
    private Long adminId;

    @Column(name="Admin_Email_Id")
    private String adminEmailId;

    @Column(name="Password")
    private String password;

    @OneToMany (mappedBy = "adminEntity", fetch = FetchType.EAGER)
    private List<RegistrationEntity> registrationEntities;

    @OneToMany (mappedBy = "adminEntity", fetch = FetchType.EAGER)
    private List<TradersEntity> tradersEntities;

    @OneToMany (mappedBy = "adminEntity", fetch = FetchType.EAGER)
    private List<IntermediaryEntity> intermediaryEntities;

    public AdminEntity() {
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getAdminEmailId() {
        return adminEmailId;
    }

    public void setAdminEmailId(String adminEmailId) {
        this.adminEmailId = adminEmailId;
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
        return adminEmailId;
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

    public List<RegistrationEntity> getRegistrationEntities() {
        return registrationEntities;
    }

    public void setRegistrationEntities(List<RegistrationEntity> registrationEntities) {
        this.registrationEntities = registrationEntities;
    }

    public List<TradersEntity> getTradersEntities() {
        return tradersEntities;
    }

    public void setTradersEntities(List<TradersEntity> tradersEntities) {
        this.tradersEntities = tradersEntities;
    }

    public List<IntermediaryEntity> getIntermediaryEntities() {
        return intermediaryEntities;
    }

    public void setIntermediaryEntities(List<IntermediaryEntity> intermediaryEntities) {
        this.intermediaryEntities = intermediaryEntities;
    }
}
