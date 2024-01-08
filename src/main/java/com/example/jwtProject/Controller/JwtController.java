package com.example.jwtProject.Controller;

import com.example.jwtProject.Entity.*;
import com.example.jwtProject.Model.JwtModel;
import com.example.jwtProject.Repository.ClientRegi;
import com.example.jwtProject.Repository.IntermediaryRepo;
import com.example.jwtProject.Repository.RequestSupportRepo;
import com.example.jwtProject.Repository.TraderRepo;
import com.example.jwtProject.Service.JwtService;
import com.example.jwtProject.Util.EmailUtil;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
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
    ClientRegi clientRegi;
    @Autowired
    TraderRepo traderRepo;
    @Autowired
    IntermediaryRepo intermediaryRepo;

    @Autowired
    private EmailUtil emailUtil;

    // API for retrieving user data
    @GetMapping("/user")
    public List<RegistrationEntity> getUser() {

        return this.jwtService.getUser();
    }
    @GetMapping("/corporateCount")
    public long getUserCount() {
        List<RegistrationEntity> users = this.jwtService.getUser();
        return users.size();
    }

    @GetMapping("/traderCount")
    public long traderCount() {
        List<TradersEntity> users = this.jwtService.traderCount();
        return users.size();
    }

    @GetMapping("/intermediaryCount")
    public long intermediaryCount() {
        List<IntermediaryEntity> users = this.jwtService.intermediaryCount();
        return users.size();
    }

    @GetMapping("/totalCount")
    public long totalCount() {
        long totalCount = 0;
        long corporateCount = clientRegi.count();
        long traderCount = traderRepo.count();
        long intermediaryCount = intermediaryRepo.count();
        totalCount = corporateCount + traderCount + intermediaryCount;
        return totalCount;
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


    @PostMapping("/deleteCorporate/{corporateId}")
    public ResponseEntity<String> deleteCorporateId(@PathVariable Long corporateId) {

        String message;

        try {
            jwtService.deleteCorporate(corporateId);
            message = "Corporate record deleted successfully.";
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception e) {
            message = "Error deleting corporate entity: " + e.getMessage();
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/deleteTrader/{traderId}")
    public ResponseEntity<String> deleteTraderId(@PathVariable Long traderId) {
        System.out.println(traderId);
        String message;

        try {
            jwtService.deleteTrader(traderId);
            message = "Trader record deleted successfully.";
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception e) {
            message = "Error deleting corporate entity: " + e.getMessage();
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/deleteIntermediary/{intermediaryId}")
    public ResponseEntity<String> deleteIntermediaryId(@PathVariable Long intermediaryId) {
        String message;

        try {
            jwtService.deleteIntermediary(intermediaryId);
            message = "Intermediary record deleted successfully.";
            return new ResponseEntity<>(message, HttpStatus.OK);
        } catch (Exception e) {
            message = "Error deleting corporate entity: " + e.getMessage();
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/saveCorporate")
    public ResponseEntity<RegistrationEntity> saveCorporate(
            @RequestParam("gst") MultipartFile gst,
            @RequestParam("financial") MultipartFile financial,
            @ModelAttribute JwtModel jwtModel) {
        try {
            RegistrationEntity registrationEntity = jwtService.saveCorporate(gst, financial, jwtModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(registrationEntity);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/saveTrader")
    public ResponseEntity<TradersEntity> saveTrader(
            @RequestParam("gst") MultipartFile gst,
            @RequestParam("financial") MultipartFile financial,
            @ModelAttribute JwtModel jwtModel) {
        try {
            TradersEntity tradersEntity = jwtService.saveTrader(gst, financial, jwtModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(tradersEntity);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/saveIntermediary")
    public ResponseEntity<IntermediaryEntity> saveIntermediary(
            @RequestParam("gst") MultipartFile gst,
            @RequestParam("financial") MultipartFile financial,
            @ModelAttribute JwtModel jwtModel) {
        try {
            IntermediaryEntity intermediaryEntity = jwtService.saveIntermediary(gst, financial, jwtModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(intermediaryEntity);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

//    @GetMapping("/returnEntity")
//    public List<Object> returnEntity(@RequestParam String entityName) {
//        List<Object> entities = new ArrayList<>();
//
//        RegistrationEntity registrationEntity = new RegistrationEntity();
//        TradersEntity tradersEntity = new TradersEntity();
//        IntermediaryEntity intermediaryEntity = new IntermediaryEntity();
//
//        if (entityName.equals("Registration_Entity")) {
//           // entities.add(registrationEntity);
//            return this.jwtService.getUser();
//        } else if (entityName.equals("Trader_Entity")) {
//            //entities.add(tradersEntity);
//            return this.jwtService.getTrader();
//        } else {
//         //   entities.add(intermediaryEntity);
//            return this.jwtService.getIntermediary();
//        }

//        return entities;
//    }


}
