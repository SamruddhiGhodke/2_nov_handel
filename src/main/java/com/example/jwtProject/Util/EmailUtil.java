package com.example.jwtProject.Util;

import com.example.jwtProject.Entity.*;
import com.example.jwtProject.Repository.*;
import com.example.jwtProject.security.JwtHelper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class EmailUtil {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ClientRegi clientRegi;
    @Autowired
    private TraderRepo traderRepo;
    @Autowired
    private IntermediaryRepo intermediaryRepo;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtHelper helper;

    @Autowired
    private RequestSupportRepo requestSupportRepo;

    @Autowired
    private AdminRepo adminRepo;

    public void sendOtpEmail(String emailId, String otp) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(emailId);
        mimeMessageHelper.setSubject("Verify OTP");
        mimeMessageHelper.setText("""
        <div>
          <a href="http://localhost:8097/verify-account?email=%s&otp=%s" target="_blank">click link to verify</a>
        </div>
        """.formatted(emailId, otp), true);

        javaMailSender.send(mimeMessage);
    }

    public void sendPasswordEmail(String emailId) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        RegistrationEntity registrationEntity = clientRegi.findByEmailId(emailId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String resetToken = generateResetToken(emailId);

        String entityName = registrationEntity.getEntityName();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(emailId);
        mimeMessageHelper.setSubject("set password");

        String emailBody = String.format("Hi %s,<br><br>", entityName);
        emailBody += "Please click the link below to set your password:<br>";
        emailBody += String.format("<a href=\"http://localhost:8097/auth/setPassword?emailId=%s&token=%s\" target=\"http://localhost:4200/resetpassword\">Click here to set your password</a>", emailId,resetToken);
        mimeMessageHelper.setText(emailBody, true);
        javaMailSender.send(mimeMessage);
    }

//    private String generateResetToken(String emailId) {
//        // Implement your logic for generating a secure reset token
//        // For simplicity, you can use UUID.randomUUID().toString() for now
//       // return UUID.randomUUID().toString();
//    }
private String generateResetToken(String email) {
        // Check if the user with the provided email exists
        clientRegi.findByEmailId(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));

        // Load user details
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        // Generate a token
        String token = helper.generateToken(userDetails);

        return token;
    }

    public void sendPasswordResetEmail(String emailId) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        RegistrationEntity registrationEntity = clientRegi.findByEmailId(emailId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String entityName = registrationEntity.getEntityName();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(emailId);
        mimeMessageHelper.setSubject("Password Reset Successfully!!");

        String emailBody = String.format("Hi %s,<br><br>", entityName);
        emailBody += "New password set successfully:<br>";
      //  emailBody += String.format("<a href=\"http://localhost:8097/setPassword?emailid=%s\" target=\"http://localhost:4200/resetpassword\">Click here to set your password</a>", emailId);
        mimeMessageHelper.setText(emailBody, true);
        javaMailSender.send(mimeMessage);
    }


    public void welcomeMail(String emailId) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        RegistrationEntity registrationEntity = clientRegi.findByEmailId(emailId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String entityName = registrationEntity.getEntityName();
        String userId = registrationEntity.getEmailId();
        System.out.println("welcome email"+registrationEntity.getAdminEntity());

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setTo(emailId);
        mimeMessageHelper.setSubject("Welcome to Handel Bidding Platform");

        // Create the HTML content for the email body
        String emailBody = "<html><body>";
        emailBody += "<p>Hi " + entityName + ",</p>";
        emailBody += "<p>We are delighted to welcome you to the Corporate Handel Bidding Platform! Your successful registration marks the beginning of an exciting journey towards new business opportunities and collaborations.</p>";
        emailBody += "<p>Your login credentials are as follows:</p>";
      //  emailBody += "<p>Please Click the link Below</p>";
        emailBody += String.format("<a href=\"http://localhost:4200/login\">Click here to login</a>");
        emailBody += "<p>User ID: " + userId + "</p>";
        emailBody += "<p> OneTime Password: admin@12345</p>";
        emailBody += "<br><p>Best regards,</p>";
        emailBody += "<p>Handel</p>";
        emailBody += "</body></html>";

        mimeMessageHelper.setText(emailBody, true);
        FileSystemResource pdfAttachment = new FileSystemResource(new File("C:\\Users\\EC21\\OneDrive - Mitrisk Consulting LLP\\Desktop\\welcome.pdf"));
        mimeMessageHelper.addAttachment("welcome.pdf", pdfAttachment);

        javaMailSender.send(mimeMessage);
    }

    public void domesticEmail(String emailId) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        RegistrationEntity registrationEntity = clientRegi.findByEmailId(emailId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String entityName = registrationEntity.getEntityName();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(emailId);
        mimeMessageHelper.setSubject("Domestic Material Registered");

        String emailBody = String.format("Hi %s,<br><br>", entityName);
        emailBody += "Domestic Material Registered<br>";
        mimeMessageHelper.setText(emailBody, true);
        javaMailSender.send(mimeMessage);
    }

    public void InternationalEmail(String emailId) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        RegistrationEntity registrationEntity = clientRegi.findByEmailId(emailId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String entityName = registrationEntity.getEntityName();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(emailId);
        mimeMessageHelper.setSubject("International Material Registered");

        String emailBody = String.format("Hi %s,<br><br>", entityName);
        emailBody += "International Material Registered <br>";
        mimeMessageHelper.setText(emailBody, true);
        javaMailSender.send(mimeMessage);
    }

    public void RequestSupportEmail(String emailId) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        RegistrationEntity registrationEntity = clientRegi.findByEmailId(emailId)
                .orElseThrow(() -> new RuntimeException("User not found"));
      //  AdminEntity adminEntity = adminRepo.findByAdminEmailId(emailId).orElseThrow(() -> new RuntimeException("user not found"));

        RequestSupportEntity requestSupportEntity = (RequestSupportEntity) requestSupportRepo.findByUserEmail(emailId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String entityName = registrationEntity.getEntityName();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo("care@actorsboard.com");
        mimeMessageHelper.setSubject("Request Support from Handel");

        String emailBody = String.format("Hi %s,<br><br>", entityName);
        emailBody += "Request Support raised!<br>";
        mimeMessageHelper.setText(emailBody, true);
        javaMailSender.send(mimeMessage);
    }

    public void welcomeMailTrader(String emailId) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        TradersEntity tradersEntity = traderRepo.findByEmailId(emailId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String entityName = tradersEntity.getEntityName();
        String userId = tradersEntity.getEmailId();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setTo(emailId);
        mimeMessageHelper.setSubject("Welcome to Handel Bidding Platform");

        // Create the HTML content for the email body
        String emailBody = "<html><body>";
        emailBody += "<p>Hi " + entityName + ",</p>";
        emailBody += "<p>We are delighted to welcome you to the Handel Bidding Platform! Your successful registration marks the beginning of an exciting journey towards new business opportunities and collaborations.</p>";
        emailBody += "<p>Your login credentials are as follows:</p>";
        emailBody += "<p>User ID: " + userId + "</p>";
        emailBody += "<p> OneTime Password: admin@12345</p>";
        emailBody += "<br><p>Best regards,</p>";
        emailBody += "<p>Handel</p>";
        emailBody += "</body></html>";

        mimeMessageHelper.setText(emailBody, true);
        javaMailSender.send(mimeMessage);
    }

    public void welcomeMailIntermediary(String emailId) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        IntermediaryEntity intermediaryEntity = intermediaryRepo.findByEmailId(emailId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String entityName = intermediaryEntity.getEntityName();
        String userId = intermediaryEntity.getEmailId();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setTo(emailId);
        mimeMessageHelper.setSubject("Welcome to Handel Bidding Platform");

        // Create the HTML content for the email body
        String emailBody = "<html><body>";
        emailBody += "<p>Hi " + entityName + ",</p>";
        emailBody += "<p>We are delighted to welcome you to the Handel Bidding Platform! Your successful registration marks the beginning of an exciting journey towards new business opportunities and collaborations.</p>";
        emailBody += "<p>Your login credentials are as follows:</p>";
        emailBody += "<p>User ID: " + userId + "</p>";
        emailBody += "<p> OneTime Password: admin@12345</p>";
        emailBody += "<br><p>Best regards,</p>";
        emailBody += "<p>Handel</p>";
        emailBody += "</body></html>";

        mimeMessageHelper.setText(emailBody, true);
        javaMailSender.send(mimeMessage);
    }


}
