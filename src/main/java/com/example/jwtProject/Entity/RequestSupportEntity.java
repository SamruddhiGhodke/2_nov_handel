package com.example.jwtProject.Entity;

import jakarta.persistence.*;


@Entity
public class RequestSupportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="supportId")
    private Long supportId;

    @Column(name="userName")
    private String userName;

    @Column(name="userEmail")
    private String userEmail;

    @Column(name="userPhone")
    private String userPhone;

    @Column(name="description")
    private String description;

    public RequestSupportEntity() {
    }

    public Long getSupportId() {
        return supportId;
    }

    public void setSupportId(Long supportId) {
        this.supportId = supportId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
}

