package com.example.jwtProject.Controller;

import com.example.jwtProject.Entity.AdminEntity;
import com.example.jwtProject.Entity.TradersEntity;
import com.example.jwtProject.Model.JwtModel;
import com.example.jwtProject.Model.JwtRequest;
import com.example.jwtProject.Model.JwtResponse;
import com.example.jwtProject.Repository.AdminRepo;
import com.example.jwtProject.Repository.TraderRepo;
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
@RequestMapping("/trader")
public class TraderController {

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
    private TraderRepo traderRepo;

    @Autowired
    private JwtService jwtService;


    @PostMapping("/traderLogin")
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
            TradersEntity tradersEntity = traderRepo.findByEmailId(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            return JwtResponse.builder()
                    .jwtToken(token)
                    .userName(userDetails.getUsername())
                    .entityName(tradersEntity.getEntityName())
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

            TradersEntity tradersEntity = traderRepo.findByEmailId(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String entityPassword = tradersEntity.getPassword();

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

    @PostMapping("/uploadFileAndUserTrader")
    public ResponseEntity<TradersEntity> uploadFileAndUserTrader(
            @RequestParam("gst") MultipartFile gst,
            @RequestParam("financial") MultipartFile financial,
            @ModelAttribute JwtModel jwtModel) {
        TradersEntity traders = null;
        try {
            traders = jwtService.uploadFileAndUserTrader(gst, financial, jwtModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(traders);
        } catch (IOException | MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/getTraders")
    public List<TradersEntity> getTrader(@RequestParam Long traderId) {

        return this.jwtService.getTrader(traderId);
    }
}
