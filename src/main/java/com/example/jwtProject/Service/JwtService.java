package com.example.jwtProject.Service;

import com.example.jwtProject.Entity.AdminEntity;
import com.example.jwtProject.Entity.IntermediaryEntity;
import com.example.jwtProject.Entity.RegistrationEntity;
import com.example.jwtProject.Entity.TradersEntity;
import com.example.jwtProject.Model.JwtModel;
import com.example.jwtProject.Repository.AdminRepo;
import com.example.jwtProject.Repository.ClientRegi;
import com.example.jwtProject.Repository.IntermediaryRepo;
import com.example.jwtProject.Repository.TraderRepo;
import com.example.jwtProject.Util.EmailUtil;
import jakarta.mail.MessagingException;
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
    @Autowired
    private TraderRepo traderRepo;
    @Autowired
    private IntermediaryRepo intermediaryRepo;

    private final String gstCertificatePath = "C:\\Users\\EC21\\OneDrive - Mitrisk Consulting LLP\\Documents\\samruddhi\\gstCertificate\\";
    private final String financialsPath = "C:\\Users\\EC21\\OneDrive - Mitrisk Consulting LLP\\Documents\\samruddhi\\financials\\";


    // API for retrieving user data
    public List<RegistrationEntity> getUser() {
        return clientRegi.findAll();
    }

    // API for uploading files and creating or updating a user
    public RegistrationEntity uploadFileAndUser(MultipartFile gst, MultipartFile financial, JwtModel jwtModel) throws IOException, MessagingException {
        String filePath = gstCertificatePath +jwtModel.getEntityName()+"_"+ gst.getOriginalFilename();
        String filePath1 = financialsPath + jwtModel.getEntityName()+"_"+financial.getOriginalFilename();
        AdminEntity adminEntity = new AdminEntity();
       // System.out.println(adminEntity.getAdminEmailId());
        RegistrationEntity registrationEntity = new RegistrationEntity();
        Optional<RegistrationEntity> corporateId = clientRegi.findByCorporateId(jwtModel.getCorporateId());
        if(corporateId.isPresent()) {
            registrationEntity.setCorporateId(jwtModel.getCorporateId());
        }
        registrationEntity.setIecCode(jwtModel.getIecCode());
        registrationEntity.setBeneficiary(jwtModel.getBeneficiary());
        registrationEntity.setUserName(jwtModel.getUserName());
        registrationEntity.setMobileNumber(jwtModel.getMobileNumber());
        registrationEntity.setEmailId(jwtModel.getEmailId());
        registrationEntity.setAccountNumber(jwtModel.getAccountNumber());
        registrationEntity.setSwiftCode(jwtModel.getSwiftCode());
        registrationEntity.setEntityName(jwtModel.getEntityName());
        registrationEntity.setGstCertificateName(registrationEntity.getEntityName()+"_" + gst.getOriginalFilename());
        registrationEntity.setFinancialName(registrationEntity.getEntityName()+"_" + financial.getOriginalFilename());
        registrationEntity.setGstCertificates(filePath);
        registrationEntity.setFinancialCertificates(filePath1);
        registrationEntity.setAdminId(Long.valueOf("1"));
        registrationEntity.setStatus("Active");
        clientRegi.save(registrationEntity);
        if (!gst.isEmpty() && !financial.isEmpty()) {
            gst.transferTo(new File(filePath));
            financial.transferTo(new File(filePath1));
        }
       // emailUtil.welcomeMail(jwtModel.getEmailId());
        emailUtil.welcomeMail(jwtModel.getEmailId());
        System.out.println("user email" + jwtModel.getEmailId());
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
    public String forgotPassword(String emailId) {

        clientRegi.findByEmailId(emailId).
                orElseThrow(() -> new RuntimeException("user not found with this email" + emailId));
        try {

            emailUtil.sendPasswordEmail(emailId);
        } catch (MessagingException e) {
            throw new RuntimeException("unable to set password try again!");
        }
        return "please check your mail to reset password";
    }


    // API for setting a new password
    public String setPassword(String emailId,String newPassword, String confirmPassword) throws MessagingException {
        RegistrationEntity registrationEntity = clientRegi.findByEmailId(emailId)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + emailId));

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Old password matches, update with new hashed password
        String newHashedPassword = passwordEncoder.encode(newPassword);
       // String confirmHashedPassword = passwordEncoder.encode(confirmPassword);
        if (newPassword.equals(confirmPassword)) {
            registrationEntity.setPassword(newHashedPassword);
            clientRegi.save(registrationEntity);
            emailUtil.sendPasswordResetEmail(emailId);
            return "New password set successfully";
        } else {
            throw new RuntimeException("new password and confirm password both are not same!!");
        }
    }

    public AdminEntity createAdmin(String emailId, String password) {
        AdminEntity adminEntity = new AdminEntity();
        adminEntity.setAdminEmailId(emailId);
        adminEntity.setPassword(password);
        return adminRepo.save(adminEntity);
    }


    public List<AdminEntity> adminUser() {
        return adminRepo.findAll();
    }

    public TradersEntity uploadFileAndUserTrader(MultipartFile gst, MultipartFile financial, JwtModel jwtModel) throws MessagingException, IOException {

        String filePath = gstCertificatePath +jwtModel.getEntityName()+"_"+ gst.getOriginalFilename();
        String filePath1 = financialsPath + jwtModel.getEntityName()+"_"+financial.getOriginalFilename();
        AdminEntity adminEntity = new AdminEntity();
        System.out.println(adminEntity.getAdminEmailId());
        TradersEntity tradersEntity = new TradersEntity();
        Optional<TradersEntity> traderId = traderRepo.findByTraderId(jwtModel.getTraderId());
        if(traderId.isPresent()) {
            tradersEntity.setTraderId(jwtModel.getTraderId());
        }
        tradersEntity.setIecCode(jwtModel.getIecCode());
        tradersEntity.setBeneficiary(jwtModel.getBeneficiary());
        tradersEntity.setUserName(jwtModel.getUserName());
        tradersEntity.setMobileNumber(jwtModel.getMobileNumber());
        tradersEntity.setEmailId(jwtModel.getEmailId());
        tradersEntity.setAccountNumber(jwtModel.getAccountNumber());
        tradersEntity.setSwiftCode(jwtModel.getSwiftCode());
        tradersEntity.setEntityName(jwtModel.getEntityName());
        tradersEntity.setGstCertificateName(tradersEntity.getEntityName() + "_" + gst.getOriginalFilename());
        tradersEntity.setFinancialName(financial.getOriginalFilename());
        tradersEntity.setGstCertificates(filePath);
        tradersEntity.setFinancialCertificates(filePath1);
        tradersEntity.setAdminId(Long.valueOf("1"));
        traderRepo.save(tradersEntity);
        if (!gst.isEmpty() && !financial.isEmpty()) {
            gst.transferTo(new File(filePath));
            financial.transferTo(new File(filePath1));
        }

        emailUtil.welcomeMailTrader(jwtModel.getEmailId());

        return tradersEntity;
    }

    public List<TradersEntity> getTrader() {
        return traderRepo.findAll();

    }

    public IntermediaryEntity uploadFileAndUserIntermediary(MultipartFile gst, MultipartFile financial, JwtModel jwtModel) throws MessagingException, IOException {

        String filePath = gstCertificatePath +jwtModel.getEntityName()+"_"+ gst.getOriginalFilename();
        String filePath1 = financialsPath + jwtModel.getEntityName()+"_"+financial.getOriginalFilename();
        AdminEntity adminEntity = new AdminEntity();
        System.out.println(adminEntity.getAdminEmailId());
        IntermediaryEntity intermediaryEntity = new IntermediaryEntity();
        Optional<IntermediaryEntity> intermediaryId = intermediaryRepo.findByIntermediaryId(jwtModel.getIntermediaryId());
        if(intermediaryId.isPresent()) {
            intermediaryEntity.setIntermediaryId(jwtModel.getIntermediaryId());
        }
        intermediaryEntity.setIecCode(jwtModel.getIecCode());
        intermediaryEntity.setBeneficiary(jwtModel.getBeneficiary());
        intermediaryEntity.setUserName(jwtModel.getUserName());
        intermediaryEntity.setMobileNumber(jwtModel.getMobileNumber());
        intermediaryEntity.setEmailId(jwtModel.getEmailId());
        intermediaryEntity.setAccountNumber(jwtModel.getAccountNumber());
        intermediaryEntity.setSwiftCode(jwtModel.getSwiftCode());
        intermediaryEntity.setEntityName(jwtModel.getEntityName());
        intermediaryEntity.setGstCertificateName(intermediaryEntity.getEntityName()+"_" + gst.getOriginalFilename());
        intermediaryEntity.setFinancialName(financial.getOriginalFilename());
        intermediaryEntity.setGstCertificates(filePath);
        intermediaryEntity.setFinancialCertificates(filePath1);
        intermediaryEntity.setAdminId(Long.valueOf("1"));
        intermediaryRepo.save(intermediaryEntity);
        if (!gst.isEmpty() && !financial.isEmpty()) {
            gst.transferTo(new File(filePath));
            financial.transferTo(new File(filePath1));
        }

        emailUtil.welcomeMailIntermediary(jwtModel.getEmailId());

        return intermediaryEntity;
    }

    public List<IntermediaryEntity> getIntermediary() {
        return intermediaryRepo.findAll();
    }

    public List<TradersEntity> traderCount() {
        return traderRepo.findAll();
    }

    public List<IntermediaryEntity> intermediaryCount() {
        return intermediaryRepo.findAll();
    }

    public void deleteCorporate(Long corporateId) {
        Optional<RegistrationEntity> registrationOptional = clientRegi.findByCorporateId(corporateId);
        System.out.println(registrationOptional);
        if(registrationOptional.isPresent()){
            RegistrationEntity registrationEntity = registrationOptional.get();
            registrationEntity.setStatus("InActive");
            clientRegi.save(registrationEntity);
            System.out.println("Deleted Successfully");
        }

       else{
           throw new RuntimeException("Given Id is not present in Database");
        }

    }

    public void deleteTrader(Long traderId) {
       // System.out.println(traderId);
        Optional<TradersEntity> tradersEntityOptional = traderRepo.findByTraderId(traderId);
        if(tradersEntityOptional.isPresent()){
            TradersEntity tradersEntity = tradersEntityOptional.get();
            tradersEntity.setStatus("InActive");
            traderRepo.save(tradersEntity);
            System.out.println("Deleted Successfully");
        }

        else{
            throw new RuntimeException("Given Id is not present in Database");
        }

    }

    public void deleteIntermediary(Long intermediaryId) {
        Optional<IntermediaryEntity> intermediaryEntityOptional = intermediaryRepo.findByIntermediaryId(intermediaryId);
        if(intermediaryEntityOptional.isPresent()){
            IntermediaryEntity intermediaryEntity = intermediaryEntityOptional.get();
            intermediaryEntity.setStatus("InActive");
           // intermediaryRepo.delete(intermediaryEntity);
            intermediaryRepo.save(intermediaryEntity);
            System.out.println("Deleted Successfully");
        }

        else{
            throw new RuntimeException("Given Id is not present in Database");
        }

    }

    public RegistrationEntity saveCorporate(MultipartFile gst, MultipartFile financial, JwtModel jwtModel) throws IOException {
        String filePath = gstCertificatePath +jwtModel.getEntityName()+"_"+ gst.getOriginalFilename();
        String filePath1 = financialsPath + jwtModel.getEntityName()+"_"+financial.getOriginalFilename();
        AdminEntity adminEntity = new AdminEntity();
        System.out.println(adminEntity.getAdminEmailId());
        RegistrationEntity registrationEntity = new RegistrationEntity();
        Optional<RegistrationEntity> corporateId = clientRegi.findByCorporateId(jwtModel.getCorporateId());
        if(corporateId.isPresent()) {
            registrationEntity.setCorporateId(jwtModel.getCorporateId());
        }
        registrationEntity.setIecCode(jwtModel.getIecCode());
        registrationEntity.setBeneficiary(jwtModel.getBeneficiary());
        registrationEntity.setUserName(jwtModel.getUserName());
        registrationEntity.setMobileNumber(jwtModel.getMobileNumber());
        registrationEntity.setEmailId(jwtModel.getEmailId());
        registrationEntity.setAccountNumber(jwtModel.getAccountNumber());
        registrationEntity.setSwiftCode(jwtModel.getSwiftCode());
        registrationEntity.setEntityName(jwtModel.getEntityName());
        registrationEntity.setGstCertificateName(registrationEntity.getEntityName()+"_" + gst.getOriginalFilename());
        registrationEntity.setFinancialName(registrationEntity.getEntityName()+"_" + financial.getOriginalFilename());
        registrationEntity.setGstCertificates(filePath);
        registrationEntity.setFinancialCertificates(filePath1);
        registrationEntity.setAdminId(Long.valueOf("1"));
        registrationEntity.setStatus("InActive");
        clientRegi.save(registrationEntity);
        if (!gst.isEmpty() && !financial.isEmpty()) {
            gst.transferTo(new File(filePath));
            financial.transferTo(new File(filePath1));
        }
        return registrationEntity;
    }

    public TradersEntity saveTrader(MultipartFile gst, MultipartFile financial, JwtModel jwtModel) throws IOException {
        String filePath = gstCertificatePath +jwtModel.getEntityName()+"_"+ gst.getOriginalFilename();
        String filePath1 = financialsPath + jwtModel.getEntityName()+"_"+financial.getOriginalFilename();
        AdminEntity adminEntity = new AdminEntity();
        System.out.println(adminEntity.getAdminEmailId());
        TradersEntity tradersEntity = new TradersEntity();
        Optional<TradersEntity> traderId = traderRepo.findByTraderId(jwtModel.getTraderId());
        if(traderId.isPresent()) {
            tradersEntity.setTraderId(jwtModel.getTraderId());
        }
        tradersEntity.setIecCode(jwtModel.getIecCode());
        tradersEntity.setBeneficiary(jwtModel.getBeneficiary());
        tradersEntity.setUserName(jwtModel.getUserName());
        tradersEntity.setMobileNumber(jwtModel.getMobileNumber());
        tradersEntity.setEmailId(jwtModel.getEmailId());
        tradersEntity.setAccountNumber(jwtModel.getAccountNumber());
        tradersEntity.setSwiftCode(jwtModel.getSwiftCode());
        tradersEntity.setEntityName(jwtModel.getEntityName());
        tradersEntity.setGstCertificateName(tradersEntity.getEntityName()+"_" + gst.getOriginalFilename());
        tradersEntity.setFinancialName(tradersEntity.getEntityName()+"_" + financial.getOriginalFilename());
        tradersEntity.setGstCertificates(filePath);
        tradersEntity.setFinancialCertificates(filePath1);
        tradersEntity.setAdminId(Long.valueOf("1"));
        tradersEntity.setStatus("InActive");
        traderRepo.save(tradersEntity);
        if (!gst.isEmpty() && !financial.isEmpty()) {
            gst.transferTo(new File(filePath));
            financial.transferTo(new File(filePath1));
        }
        return tradersEntity;
    }

    public IntermediaryEntity saveIntermediary(MultipartFile gst, MultipartFile financial, JwtModel jwtModel) throws IOException {
        String filePath = gstCertificatePath +jwtModel.getEntityName()+"_"+ gst.getOriginalFilename();
        String filePath1 = financialsPath + jwtModel.getEntityName()+"_"+financial.getOriginalFilename();
        AdminEntity adminEntity = new AdminEntity();
        System.out.println(adminEntity.getAdminEmailId());
        IntermediaryEntity intermediaryEntity = new IntermediaryEntity();
        Optional<IntermediaryEntity> intermediaryId = intermediaryRepo.findByIntermediaryId(jwtModel.getIntermediaryId());
        if(intermediaryId.isPresent()) {
            intermediaryEntity.setIntermediaryId(jwtModel.getIntermediaryId());
        }
        intermediaryEntity.setIecCode(jwtModel.getIecCode());
        intermediaryEntity.setBeneficiary(jwtModel.getBeneficiary());
        intermediaryEntity.setUserName(jwtModel.getUserName());
        intermediaryEntity.setMobileNumber(jwtModel.getMobileNumber());
        intermediaryEntity.setEmailId(jwtModel.getEmailId());
        intermediaryEntity.setAccountNumber(jwtModel.getAccountNumber());
        intermediaryEntity.setSwiftCode(jwtModel.getSwiftCode());
        intermediaryEntity.setEntityName(jwtModel.getEntityName());
        intermediaryEntity.setGstCertificateName(intermediaryEntity.getEntityName()+"_" + gst.getOriginalFilename());
        intermediaryEntity.setFinancialName(intermediaryEntity.getEntityName()+"_" + financial.getOriginalFilename());
        intermediaryEntity.setGstCertificates(filePath);
        intermediaryEntity.setFinancialCertificates(filePath1);
        intermediaryEntity.setAdminId(Long.valueOf("1"));
        intermediaryEntity.setStatus("InActive");
        intermediaryRepo.save(intermediaryEntity);
        if (!gst.isEmpty() && !financial.isEmpty()) {
            gst.transferTo(new File(filePath));
            financial.transferTo(new File(filePath1));
        }
        return intermediaryEntity;
    }
}
