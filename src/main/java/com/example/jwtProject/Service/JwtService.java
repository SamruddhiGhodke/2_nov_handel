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
import java.util.ArrayList;
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
    public List<RegistrationEntity> getUser(Long corporateId) {

        if(corporateId==0){
            return clientRegi.findAll();
        }
        else {
            Optional<RegistrationEntity> corporateId1 = clientRegi.findByCorporateId(corporateId);
            if(corporateId1.isPresent()){
                List<RegistrationEntity> resultList = new ArrayList<>();
                resultList.add(corporateId1.get());
                return resultList;

            }
            else {
                throw new RuntimeException("Corporate Id is not present" +corporateId);
            }
        }

    }

    // API for uploading files and creating or updating a user
    public RegistrationEntity uploadFileAndUser(MultipartFile gst, MultipartFile financial, JwtModel jwtModel) throws IOException, MessagingException {
        String filePath = gstCertificatePath +jwtModel.getEntityName()+"_"+ gst.getOriginalFilename();
        String filePath1 = financialsPath + jwtModel.getEntityName()+"_"+financial.getOriginalFilename();
        AdminEntity adminEntity = new AdminEntity();
        RegistrationEntity registrationEntity = new RegistrationEntity();
        Optional<RegistrationEntity> corporateId = clientRegi.findByCorporateId(jwtModel.getCorporateId());
        Optional<RegistrationEntity> emailId = clientRegi.findByEmailId(jwtModel.getEmailId());
        if(corporateId.isPresent() || emailId.isPresent()) {
            registrationEntity.setCorporateId(jwtModel.getCorporateId());
            registrationEntity.setIecCode(jwtModel.getIecCode());
            registrationEntity.setBeneficiary(jwtModel.getBeneficiary());
            registrationEntity.setUserName(jwtModel.getUserName());
            registrationEntity.setMobileNumber(jwtModel.getMobileNumber());
            registrationEntity.setEmailId(jwtModel.getEmailId());
            registrationEntity.setAccountNumber(jwtModel.getAccountNumber());
            registrationEntity.setSwiftCode(jwtModel.getSwiftCode());
            registrationEntity.setEntityName(jwtModel.getEntityName());
            registrationEntity.setGstCertificateName(gst.getOriginalFilename());
            registrationEntity.setFinancialName(financial.getOriginalFilename());
            registrationEntity.setGstCertificates(filePath);
            registrationEntity.setFinancialCertificates(filePath1);
            registrationEntity.setAdminId(Long.valueOf("1"));
            registrationEntity.setStatus("Active");
            clientRegi.save(registrationEntity);
        }
        else {
            RegistrationEntity registration = new RegistrationEntity();
            registration.setIecCode(jwtModel.getIecCode());
            registration.setBeneficiary(jwtModel.getBeneficiary());
            registration.setUserName(jwtModel.getUserName());
            registration.setMobileNumber(jwtModel.getMobileNumber());
            registration.setEmailId(jwtModel.getEmailId());
            registration.setAccountNumber(jwtModel.getAccountNumber());
            registration.setSwiftCode(jwtModel.getSwiftCode());
            registration.setEntityName(jwtModel.getEntityName());
            registration.setGstCertificateName(jwtModel.getEntityName()+ "_" + gst.getOriginalFilename());
            registration.setFinancialName(jwtModel.getEntityName()+ "_" + financial.getOriginalFilename());
            registration.setGstCertificates(filePath);
            registration.setFinancialCertificates(filePath1);
            registration.setAdminId(Long.valueOf("1"));
            registration.setStatus("Active");
            clientRegi.save(registration);
            emailUtil.welcomeMail(jwtModel.getEmailId());

        }
        if (!gst.isEmpty() && !financial.isEmpty()) {
            gst.transferTo(new File(filePath));
            financial.transferTo(new File(filePath1));
        }
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
            registrationEntity.setLoginCount("0");
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
        Optional<TradersEntity> emailId = traderRepo.findByEmailId(jwtModel.getEmailId());
        if(traderId.isPresent() || emailId.isPresent()) {
            tradersEntity.setTraderId(jwtModel.getTraderId());
            tradersEntity.setIecCode(jwtModel.getIecCode());
            tradersEntity.setBeneficiary(jwtModel.getBeneficiary());
            tradersEntity.setUserName(jwtModel.getUserName());
            tradersEntity.setMobileNumber(jwtModel.getMobileNumber());
            tradersEntity.setEmailId(jwtModel.getEmailId());
            tradersEntity.setAccountNumber(jwtModel.getAccountNumber());
            tradersEntity.setSwiftCode(jwtModel.getSwiftCode());
            tradersEntity.setEntityName(jwtModel.getEntityName());
            tradersEntity.setGstCertificateName(jwtModel.getEntityName()+"_" + gst.getOriginalFilename());
            tradersEntity.setFinancialName(jwtModel.getEntityName()+"_" +financial.getOriginalFilename());
            tradersEntity.setGstCertificates(filePath);
            tradersEntity.setFinancialCertificates(filePath1);
            tradersEntity.setStatus("Active");
            tradersEntity.setAdminId(Long.valueOf("1"));
            traderRepo.save(tradersEntity);
        }
        else {
            TradersEntity traders = new TradersEntity();
            traders.setIecCode(jwtModel.getIecCode());
            traders.setBeneficiary(jwtModel.getBeneficiary());
            traders.setUserName(jwtModel.getUserName());
            traders.setMobileNumber(jwtModel.getMobileNumber());
            traders.setEmailId(jwtModel.getEmailId());
            traders.setAccountNumber(jwtModel.getAccountNumber());
            traders.setSwiftCode(jwtModel.getSwiftCode());
            traders.setEntityName(jwtModel.getEntityName());
            traders.setGstCertificateName(jwtModel.getEntityName()+ "_" + gst.getOriginalFilename());
            traders.setFinancialName(jwtModel.getEntityName()+ "_" + financial.getOriginalFilename());
            traders.setGstCertificates(filePath);
            traders.setFinancialCertificates(filePath1);
            traders.setAdminId(Long.valueOf("1"));
            traders.setStatus("Active");
            traderRepo.save(traders);
            emailUtil.welcomeMailTrader(jwtModel.getEmailId());
        }
        if (!gst.isEmpty() && !financial.isEmpty()) {
            gst.transferTo(new File(filePath));
            financial.transferTo(new File(filePath1));
        }

        return tradersEntity;
    }

    public List<TradersEntity> getTrader(Long traderId) {

        if(traderId==0){
            return traderRepo.findAll();
        }
        else {
            Optional<TradersEntity> traders = traderRepo.findByTraderId(traderId);
            if(traders.isPresent()){
                List<TradersEntity> resultList = new ArrayList<>();
                resultList.add(traders.get());
                return resultList;

            }
            else {
                throw new RuntimeException("trader Id is not present" +traderId);
            }
        }

    }

    public IntermediaryEntity uploadFileAndUserIntermediary(MultipartFile gst, MultipartFile financial, JwtModel jwtModel) throws MessagingException, IOException {

        String filePath = gstCertificatePath +jwtModel.getEntityName()+"_"+ gst.getOriginalFilename();
        String filePath1 = financialsPath + jwtModel.getEntityName()+"_"+financial.getOriginalFilename();
        AdminEntity adminEntity = new AdminEntity();
        System.out.println(adminEntity.getAdminEmailId());
        IntermediaryEntity intermediaryEntity = new IntermediaryEntity();
        Optional<IntermediaryEntity> intermediaryId = intermediaryRepo.findByIntermediaryId(jwtModel.getIntermediaryId());
        Optional<IntermediaryEntity> emailId = intermediaryRepo.findByEmailId(jwtModel.getEmailId());
        if(intermediaryId.isPresent() || emailId.isPresent()) {
            intermediaryEntity.setIntermediaryId(jwtModel.getIntermediaryId());
            intermediaryEntity.setIecCode(jwtModel.getIecCode());
            intermediaryEntity.setBeneficiary(jwtModel.getBeneficiary());
            intermediaryEntity.setUserName(jwtModel.getUserName());
            intermediaryEntity.setMobileNumber(jwtModel.getMobileNumber());
            intermediaryEntity.setEmailId(jwtModel.getEmailId());
            intermediaryEntity.setAccountNumber(jwtModel.getAccountNumber());
            intermediaryEntity.setSwiftCode(jwtModel.getSwiftCode());
            intermediaryEntity.setEntityName(jwtModel.getEntityName());
            intermediaryEntity.setGstCertificateName(jwtModel.getEntityName()+"_" + gst.getOriginalFilename());
            intermediaryEntity.setFinancialName(jwtModel.getEntityName()+"_" + financial.getOriginalFilename());
            intermediaryEntity.setGstCertificates(filePath);
            intermediaryEntity.setFinancialCertificates(filePath1);
            intermediaryEntity.setAdminId(Long.valueOf("1"));
            intermediaryEntity.setStatus("Active");
            intermediaryRepo.save(intermediaryEntity);
        }
        else {
            IntermediaryEntity intermediary = new IntermediaryEntity();
            intermediary.setIecCode(jwtModel.getIecCode());
            intermediary.setBeneficiary(jwtModel.getBeneficiary());
            intermediary.setUserName(jwtModel.getUserName());
            intermediary.setMobileNumber(jwtModel.getMobileNumber());
            intermediary.setEmailId(jwtModel.getEmailId());
            intermediary.setAccountNumber(jwtModel.getAccountNumber());
            intermediary.setSwiftCode(jwtModel.getSwiftCode());
            intermediary.setEntityName(jwtModel.getEntityName());
            intermediary.setGstCertificateName(jwtModel.getEntityName()+"_" + gst.getOriginalFilename());
            intermediary.setFinancialName(jwtModel.getEntityName()+"_" + financial.getOriginalFilename());
            intermediary.setGstCertificates(filePath);
            intermediary.setFinancialCertificates(filePath1);
            intermediary.setAdminId(Long.valueOf("1"));
            intermediary.setStatus("Active");
            intermediaryRepo.save(intermediary);
            emailUtil.welcomeMailIntermediary(jwtModel.getEmailId());
        }
        if (!gst.isEmpty() && !financial.isEmpty()) {
            gst.transferTo(new File(filePath));
            financial.transferTo(new File(filePath1));
        }

        return intermediaryEntity;
    }

    public List<IntermediaryEntity> getIntermediary(Long intermediaryId) {

        if(intermediaryId==0){
            return intermediaryRepo.findAll();
        }
        else {
            Optional<IntermediaryEntity> traders = intermediaryRepo.findByIntermediaryId(intermediaryId);
            if(traders.isPresent()){
                List<IntermediaryEntity> resultList = new ArrayList<>();
                resultList.add(traders.get());
                return resultList;

            }
            else {
                throw new RuntimeException("intermediary Id is not present" +intermediaryId);
            }
        }
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

    public Optional<AdminEntity> getAdminByEmail(String email) {
        return adminRepo.findByAdminEmailId(email);
    }

    public List<RegistrationEntity> getUserCount() {
        return clientRegi.findAll();
    }
}
