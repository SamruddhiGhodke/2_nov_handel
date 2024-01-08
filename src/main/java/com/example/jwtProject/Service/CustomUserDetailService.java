package com.example.jwtProject.Service;

import com.example.jwtProject.Entity.AdminEntity;
import com.example.jwtProject.Entity.IntermediaryEntity;
import com.example.jwtProject.Entity.RegistrationEntity;
import com.example.jwtProject.Entity.TradersEntity;
import com.example.jwtProject.Repository.AdminRepo;
import com.example.jwtProject.Repository.ClientRegi;
import com.example.jwtProject.Repository.IntermediaryRepo;
import com.example.jwtProject.Repository.TraderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    public ClientRegi clientRegi;

    @Autowired
    public AdminRepo adminRepo;
    @Autowired
    public TraderRepo traderRepo;
    @Autowired
    public IntermediaryRepo intermediaryRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = null;

        if ("admin@handel.co.in".equals(username)) {
            AdminEntity adminEntity = adminRepo.findByAdminEmailId(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Admin user not found"));
            userDetails = adminEntity;
        } else {
            Optional<TradersEntity> tradersEntityOptional = traderRepo.findByEmailId(username);
            if (tradersEntityOptional.isPresent()) {
                userDetails = tradersEntityOptional.get();
            } else {
                Optional<IntermediaryEntity> intermediaryEntityOptional = intermediaryRepo.findByEmailId(username);
                if (tradersEntityOptional.isPresent()) {
                    userDetails = intermediaryEntityOptional.get();
                } else {
                    RegistrationEntity registrationEntity = clientRegi.findByEmailId(username)
                            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                    userDetails = registrationEntity;
                }
            }
        }
        return userDetails;
    }
}
