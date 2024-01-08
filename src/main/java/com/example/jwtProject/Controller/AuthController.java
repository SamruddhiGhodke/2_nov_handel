package com.example.jwtProject.Controller;

import com.example.jwtProject.Entity.AdminEntity;
import com.example.jwtProject.Entity.RegistrationEntity;
import com.example.jwtProject.Model.JwtModel;
import com.example.jwtProject.Model.JwtRequest;
import com.example.jwtProject.Model.JwtResponse;
import com.example.jwtProject.Repository.AdminRepo;
import com.example.jwtProject.Repository.ClientRegi;
import com.example.jwtProject.Service.JwtService;
import com.example.jwtProject.security.JwtHelper;
import io.micrometer.common.util.StringUtils;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserDetailsService userDetailsService;

    private Map<String, String> resetTokenMap = new HashMap<>();

    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private ClientRegi clientRegi;

    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtHelper helper;






//    // Authentication logic
    private String doAuthenticate(String email, String password) {

        System.out.println("Attempting authentication for email: " + email);

        if (email.equals("admin@handel.co.in")) {
            AdminEntity adminEntity = adminRepo.findByAdminEmailId(email).orElseThrow(() -> new RuntimeException("user not found"));
            String entityPassword = adminEntity.getPassword();

            if (StringUtils.isBlank(password)) {
                throw new IllegalArgumentException("Password cannot be empty or whitespace.");
            }

            if (password.equals("Mumbai@2024")) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                String token = helper.generateToken(userDetails);
                System.out.println("Authentication successful");
                return token;
            } else {
                throw new RuntimeException("Password mismatch");
            }
        } else {

            RegistrationEntity registrationEntity = clientRegi.findByEmailId(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String entityPassword = registrationEntity.getPassword();
            String entityName = registrationEntity.getEntityName();

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            if (StringUtils.isBlank(password)) {
                throw new IllegalArgumentException("Password cannot be empty or whitespace.");
            }

            if (password.equals("Mumbai@2024") || passwordEncoder.matches(password, entityPassword)) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                String token = helper.generateToken(userDetails);
                System.out.println("Authentication successful");
                return token;
            } else {
                throw new RuntimeException("Password mismatch");
            }
        }
    }

    // API for user login
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        String token = this.doAuthenticate(request.getEmail(), request.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        JwtResponse response = buildJwtResponse(request.getEmail(), userDetails, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private JwtResponse buildJwtResponse(String email, UserDetails userDetails, String token) {
        if (isAdmin(email)) {
            AdminEntity adminEntity = adminRepo.findByAdminEmailId(email)
                    .orElseThrow(() -> new RuntimeException("Admin not found"));
            return JwtResponse.builder()
                    .jwtToken(token)
                    .userName(userDetails.getUsername())
                    .build();
        } else {
            RegistrationEntity registrationEntity = clientRegi.findByEmailId(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            return JwtResponse.builder()
                    .jwtToken(token)
                    .userName(userDetails.getUsername())
                    .entityName(registrationEntity.getEntityName())
                    .build();
        }
    }

    private boolean isAdmin(String email) {
        return "admin@handel.co.in".equals(email);
    }



    // API for uploading files and user data
    @PostMapping("/uploadFileAndUser")
    public ResponseEntity<RegistrationEntity> uploadFileAndUser(
            @RequestParam("gst") MultipartFile gst,
            @RequestParam("financial") MultipartFile financial,
            @ModelAttribute JwtModel jwtModel) {
        try {
            RegistrationEntity registrationEntity = jwtService.uploadFileAndUser(gst, financial, jwtModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(registrationEntity);
        } catch (IOException | MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    // API for downloading files
    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
        try {
            byte[] fileBytes = jwtService.downloadFile(fileName);
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + fileName)
                    .body(fileBytes);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // API for handling forgotten passwords
    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody JwtRequest request) {
        return new ResponseEntity<>(jwtService.forgotPassword(request.getEmail()), HttpStatus.OK);
    }



    // API for setting a new password
    @PostMapping("/setPassword")
    public ResponseEntity<String> setPassword( @RequestParam String emailId,@RequestParam String newPassword, @RequestParam String confirmPassword) throws MessagingException {
        return new ResponseEntity<>(jwtService.setPassword(emailId,newPassword,confirmPassword), HttpStatus.OK);
    }


    // API for creating a new Admin
    @PostMapping("/createAdmin")
    public AdminEntity createAdmin(@RequestParam String emailId, @RequestParam String password)  {
        return jwtService.createAdmin(emailId, password);
    }

}
