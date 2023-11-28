package com.example.jwtProject.Controller;

import com.example.jwtProject.Entity.AdminEntity;
import com.example.jwtProject.Entity.RegistrationEntity;
import com.example.jwtProject.Entity.RequestSupportEntity;
import com.example.jwtProject.Repository.RequestSupportRepo;
import com.example.jwtProject.Service.JwtService;
import com.example.jwtProject.Util.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@SessionAttributes("msg")
@RequestMapping("/home")
public class JwtController {
    @Autowired
    JwtService jwtService;

    @Autowired
    RequestSupportRepo requestSupportRepo;

    @Autowired
    private EmailUtil emailUtil;

    // API for retrieving user data
    @GetMapping("/user")
    public List<RegistrationEntity> getUser() {
        return this.jwtService.getUser();
    }

    @GetMapping("/adminUser")
    public List<AdminEntity> adminUser() {
        return this.jwtService.adminUser();
    }

    // API for retrieving the currently logged in user
    @GetMapping("/currentUser")
    public String getLoggedInUser(Principal principal) {
        return principal.getName();
    }

    @PostMapping("/submit")
    public ResponseEntity<String> submitSupportRequest(@RequestBody RequestSupportEntity requestSupportEntity) {
        try {
            requestSupportRepo.save(requestSupportEntity);
            emailUtil.RequestSupportEmail(requestSupportEntity.getUserEmail());
            return ResponseEntity.ok("Support request submitted successfully!");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error submitting support request: " + e.getMessage());
        }
    }
}
