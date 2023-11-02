package com.example.jwtProject.Service;


import com.example.jwtProject.Entity.AdminEntity;
import com.example.jwtProject.Entity.RegistrationEntity;
import com.example.jwtProject.Model.JwtModel;
import com.example.jwtProject.Repository.AdminRepo;
import com.example.jwtProject.Repository.ClientRegi;
import com.example.jwtProject.Util.EmailUtil;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;


@Service
public class JwtService {
   @Autowired
    private ClientRegi clientRegi;

    @Autowired
    private AdminRepo adminRepo;
   @Autowired
   private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailUtil emailUtil;

    private final String gstCertificatePath="C:\\Users\\EC21\\OneDrive - Mitrisk Consulting LLP\\Documents\\samruddhi\\gstCertificate";
    private final String financialsPath="C:\\Users\\EC21\\OneDrive - Mitrisk Consulting LLP\\Documents\\samruddhi\\financials\\";



    // API for retrieving user data
    public List<RegistrationEntity> getUser(){
        return clientRegi.findAll();
    }

    // API for creating or updating a user
    public RegistrationEntity createUser(JwtModel jwtModel) throws MessagingException {
        Long id = jwtModel.getId();

        if (id != null && id != 0) {
            Optional<RegistrationEntity> registrationOptional = clientRegi.findById(id);

            if (registrationOptional.isPresent()) {
                RegistrationEntity registration = registrationOptional.get();
                registration.setEntityName(jwtModel.getEntityName());
                registration.setIecCode(jwtModel.getIecCode());
                registration.setGstCertificates(jwtModel.getGstCertificates());
                registration.setFinancialCertificates(jwtModel.getFinancialCertificates());
                registration.setUserName(jwtModel.getUserName());
                registration.setMobileNumber(jwtModel.getMobileNumber());
                registration.setEmailId(jwtModel.getEmailId());
                registration.setAccountNumber(jwtModel.getAccountNumber());
                registration.setBeneficiary(jwtModel.getBeneficiary());
                registration.setSwiftCode(jwtModel.getSwiftCode());
                registration.setDomesticMaterialId(jwtModel.getDomesticMaterialId());
                registration.setInternationalMaterialId(jwtModel.getInternationalMaterialId());
//                registration.setPassword(passwordEncoder.encode(jwtModel.getPassword()));
                return clientRegi.save(registration);

            } else {
                throw new EntityNotFoundException("User with ID " + id + " not found");
            }
        } else {
            RegistrationEntity registrationEntity = new RegistrationEntity();
            registrationEntity.setEntityName(jwtModel.getEntityName());
            registrationEntity.setIecCode(jwtModel.getIecCode());
            registrationEntity.setGstCertificates(jwtModel.getGstCertificates());
            registrationEntity.setFinancialCertificates(jwtModel.getFinancialCertificates());
            registrationEntity.setUserName(jwtModel.getUserName());
            registrationEntity.setMobileNumber(jwtModel.getMobileNumber());
            registrationEntity.setEmailId(jwtModel.getEmailId());
            registrationEntity.setAccountNumber(jwtModel.getAccountNumber());
            registrationEntity.setBeneficiary(jwtModel.getBeneficiary());
            registrationEntity.setSwiftCode(jwtModel.getSwiftCode());
            registrationEntity.setDomesticMaterialId(jwtModel.getDomesticMaterialId());
            registrationEntity.setInternationalMaterialId(jwtModel.getInternationalMaterialId());
            emailUtil.sendPasswordResetEmail(jwtModel.getEmailId());
            return clientRegi.save(registrationEntity);
        }
    }


    // API for uploading files and creating or updating a user
    public RegistrationEntity uploadFileAndUser(MultipartFile gst,MultipartFile financial, JwtModel jwtModel) throws IOException, MessagingException {
        String filePath=gstCertificatePath+gst.getOriginalFilename();
        String filePath1=financialsPath+financial.getOriginalFilename();
        RegistrationEntity registrationEntity = new RegistrationEntity();
        registrationEntity.setIecCode(jwtModel.getIecCode());
        registrationEntity.getBeneficiary(jwtModel.getBeneficiary());
        registrationEntity.setUserName(jwtModel.getUserName());
        registrationEntity.setMobileNumber(jwtModel.getMobileNumber());
        registrationEntity.setEmailId(jwtModel.getEmailId());
        registrationEntity.setAccountNumber(jwtModel.getAccountNumber());
        registrationEntity.setSwiftCode(jwtModel.getSwiftCode());
        registrationEntity.setEntityName(jwtModel.getEntityName());
        registrationEntity.setGstCertificateName(registrationEntity.getEntityName()+gst.getOriginalFilename());
        registrationEntity.setFinancialName(financial.getOriginalFilename());
        registrationEntity.setGstCertificates(filePath);
        registrationEntity.setFinancialCertificates(filePath1);
       // registrationEntity.setPassword(passwordEncoder.encode(jwtModel.getPassword()));

//        registrationEntity.setInternationalMaterialId(jwtModel.getInternationalMaterialId());
//        registrationEntity.setDomesticMaterialId(jwtModel.getDomesticMaterialId());
        clientRegi.save(registrationEntity);
        if (!gst.isEmpty() && !financial.isEmpty()) {
            gst.transferTo(new File(filePath));
            financial.transferTo(new File(filePath1));
        }

        emailUtil.welcomeMail(jwtModel.getEmailId());

        return registrationEntity;

    }
    // API for downloading a file
    public byte[] downloadFile(String fileName) throws IOException {
        RegistrationEntity dbGstName = clientRegi.findByGstCertificateName(fileName);

        if (dbGstName == null) {
            throw new FileNotFoundException("File not found");
        }

        String gstCertificate = dbGstName.getGstCertificates();
        return Files.readAllBytes(new File(gstCertificate).toPath());
    }


    // API for handling forgotten passwords
    public String forgotPassword(String emailId){

        clientRegi.findByEmailId(emailId).
                orElseThrow(()->new RuntimeException("user not found with this email"+ emailId));
        try{
            emailUtil.sendPasswordEmail(emailId);
        } catch (MessagingException e) {
            throw new RuntimeException("unable to set password try again!");
        }
        return "please check your mail to reset password";

    }

    // Assuming you have a BCryptPasswordEncoder bean configured
    // API for setting a new password
    public String setPassword(String emailId, String newPassword) throws MessagingException {
        RegistrationEntity registrationEntity = clientRegi.findByEmailId(emailId)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + emailId));

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            // Old password matches, update with new hashed password
            String newHashedPassword = passwordEncoder.encode(newPassword);
            registrationEntity.setPassword(newHashedPassword);
            clientRegi.save(registrationEntity);
            emailUtil.sendPasswordResetEmail(emailId);
            return "New password set successfully";
        }

    public AdminEntity createAdmin(String emailId, String password) {
        AdminEntity adminEntity = new AdminEntity();
        adminEntity.setAdminEmailId(emailId);
        adminEntity.setPassword(password);
        return adminRepo.save(adminEntity);
    }
}
