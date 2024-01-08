package com.example.jwtProject.Controller;

import com.example.jwtProject.Entity.AdminEntity;
import com.example.jwtProject.Entity.IntermediaryEntity;
import com.example.jwtProject.Entity.TradersEntity;
import com.example.jwtProject.Model.JwtModel;
import com.example.jwtProject.Model.JwtRequest;
import com.example.jwtProject.Model.JwtResponse;
import com.example.jwtProject.Repository.AdminRepo;
import com.example.jwtProject.Repository.IntermediaryRepo;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/intermediary")
public class IntermediaryController {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtHelper helper;
    @Autowired
    private IntermediaryRepo intermediaryRepo;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/intermediaryLogin")
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
            IntermediaryEntity intermediaryEntity = intermediaryRepo.findByEmailId(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            return JwtResponse.builder()
                    .jwtToken(token)
                    .userName(userDetails.getUsername())
                    .entityName(intermediaryEntity.getEntityName())
                    .build();
        }
    }

    private boolean isAdmin(String email) {
        return "admin@handel.co.in".equals(email);
    }
    // Authentication logic
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

            IntermediaryEntity intermediaryEntity = intermediaryRepo.findByEmailId(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String entityPassword = intermediaryEntity.getPassword();

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
        }
    }

    @PostMapping("/uploadFileAndUserIntermediary")
    public ResponseEntity<IntermediaryEntity> uploadFileAndUserIntermediary(
            @RequestParam("gst") MultipartFile gst,
            @RequestParam("financial") MultipartFile financial,
            @ModelAttribute JwtModel jwtModel) {
        try {
            IntermediaryEntity intermediaryEntity = jwtService.uploadFileAndUserIntermediary(gst, financial, jwtModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(intermediaryEntity);
        } catch (IOException | MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getIntermediary")
    public List<IntermediaryEntity> getIntermediary() {
        return this.jwtService.getIntermediary();
    }
}
