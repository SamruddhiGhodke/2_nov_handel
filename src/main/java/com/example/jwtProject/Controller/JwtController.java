package com.example.jwtProject.Controller;

import com.example.jwtProject.Entity.RegistrationEntity;
import com.example.jwtProject.Service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@SessionAttributes("msg")
@RequestMapping("/home")
public class JwtController {
    @Autowired
    JwtService jwtService;

    // API for retrieving user data
    @GetMapping("/user")
    public List<RegistrationEntity> getUser(){
        return this.jwtService.getUser();
    }

    // API for retrieving the currently logged in user
    @GetMapping("/currentUser")
    public String getLoggedInUser(Principal principal){
        return principal.getName();
    }
}
