package com.example.jwtProject.Util;

import com.example.jwtProject.Entity.RegistrationEntity;
import com.example.jwtProject.Repository.ClientRegi;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;


@Component
public class EmailUtil {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private ClientRegi clientRegi;

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

        String entityName = registrationEntity.getEntityName();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(emailId);
        mimeMessageHelper.setSubject("set password");

        String emailBody = String.format("Hi %s,<br><br>", entityName);
        emailBody += "Please click the link below to set your password:<br>";
        emailBody += String.format("<a href=\"http://localhost:8097/setPassword?emailid=%s\" target=\"http://localhost:4200/resetpassword\">Click here to set your password</a>", emailId);
        mimeMessageHelper.setText(emailBody, true);
        javaMailSender.send(mimeMessage);
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
//        emailBody += String.format("<a href=\"http://localhost:8097/setPassword?emailid=%s\" target=\"http://localhost:4200/resetpassword\">Click here to set your password</a>", emailId);
        mimeMessageHelper.setText(emailBody, true);
        javaMailSender.send(mimeMessage);
    }


    public void welcomeMail(String emailId) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        RegistrationEntity registrationEntity = clientRegi.findByEmailId(emailId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String entityName = registrationEntity.getEntityName();
        String userId = registrationEntity.getEmailId();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setTo(emailId);
        mimeMessageHelper.setSubject("Welcome to Handel Bidding Platform");

        // Create the HTML content for the email body
        String emailBody = "<html><body>";
        emailBody += "<p>Hi " + entityName + ",</p>";
        emailBody += "<p>We are delighted to welcome you to the Handel Bidding Platform! Your successful registration marks the beginning of an exciting journey towards new business opportunities and collaborations.</p>";
        emailBody += "<p>Your login credentials are as follows:</p>";
        emailBody += "<p>User ID: " + userId + "</p>";
        emailBody += "<p>Password: 123456</p>";
        emailBody += "<br><p>Best regards,</p>";
        emailBody += "<p>Handel</p>";
        emailBody += "</body></html>";

        mimeMessageHelper.setText(emailBody, true);
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
//
//    public void InternationalEmail(String emailId) throws MessagingException {
//        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
//        RegistrationEntity registrationEntity = clientRegi.findByEmailId(emailId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        String entityName = registrationEntity.getEntityName();
//        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
//        mimeMessageHelper.setTo(emailId);
//        mimeMessageHelper.setSubject("International Material Registered");
//
//        String emailBody = String.format("Hi %s,<br><br>", entityName);
//        emailBody += "International Material Registered <br>";
//        mimeMessageHelper.setText(emailBody, true);
//        javaMailSender.send(mimeMessage);
//    }

}
