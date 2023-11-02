package com.example.jwtProject.Service;

import com.example.jwtProject.Entity.AdminEntity;
import com.example.jwtProject.Entity.RegistrationEntity;
import com.example.jwtProject.Repository.AdminRepo;
import com.example.jwtProject.Repository.ClientRegi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    public ClientRegi clientRegi;

    @Autowired
    public AdminRepo adminRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.equals("admin@handel.co.in")) {
            AdminEntity adminEntity = adminRepo.findByAdminEmailId(username).orElseThrow(() -> new RuntimeException("user not found"));
            return adminEntity;
        } else {
            RegistrationEntity registrationEntity = clientRegi.findByEmailId(username).orElseThrow(() -> new RuntimeException("user not found"));
            return registrationEntity;
        }
    }


}
