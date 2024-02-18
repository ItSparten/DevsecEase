package com.cozy.controllers;

import com.cozy.dto.request.RentPropertyRequest;
import com.cozy.dto.request.ResetPasswordRequest;
import com.cozy.dto.request.UpdatePasswordRequest;
import com.cozy.dto.response.SuccessMessageResponse;
import com.cozy.services.PasswordService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/password")
@RequiredArgsConstructor
public class ResetPasswordController {

    private final PasswordService passwordService;
    @PostMapping("/reset")
    public ResponseEntity<SuccessMessageResponse> reset(@RequestBody ResetPasswordRequest request) throws MessagingException {
        passwordService.resetPassword(request.getEmail());
        return ResponseEntity.ok(new SuccessMessageResponse("password sent successfully."));
    }
   /* @PostMapping("/update")
    public ResponseEntity<String> updatePassword(@RequestBody UpdatePasswordRequest request) {
        // Vérifier le mot de passe actuel
        if (!passwordService.checkCurrentPassword(request.getUserId(), request.getCurrentPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Mot de passe actuel incorrect.");
        }
        // Mettre à jour le mot de passe
        passwordService.updatePassword(request.getUserId(), request.getNewPassword());
        return ResponseEntity.ok("Mot de passe mis à jour avec succès.");
    }*/
  /* @PostMapping("/update")
   public ResponseEntity<Map<String, String>> updatePassword(@RequestBody UpdatePasswordRequest request) {
       Map<String, String> response = new HashMap<>();

       // Vérifier le mot de passe actuel
       if (!passwordService.checkCurrentPassword(request.getUserId(), request.getCurrentPassword())) {
           response.put("status", "error");
           response.put("message", "Mot de passe actuel incorrect.");
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
       }

       // Mettre à jour le mot de passe
       passwordService.updatePassword(request.getUserId(), request.getNewPassword());

       response.put("status", "success");
       response.put("message", "Mot de passe mis à jour avec succès.");

       return ResponseEntity.ok(response);
   }*/

    @PostMapping("/update")
    public ResponseEntity<Map<String, String>> updatePassword(@RequestBody UpdatePasswordRequest request) {
        Map<String, String> response = new HashMap<>();

        // Vérifier le mot de passe actuel
        if (!passwordService.checkCurrentPassword(request.getUserId(), request.getCurrentPassword())) {
            response.put("status", "error");
            response.put("errorType", "InvalidCurrentPassword");
            response.put("message", "Mot de passe actuel incorrect.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Mettre à jour le mot de passe
        passwordService.updatePassword(request.getUserId(), request.getNewPassword());

        response.put("status", "success");
        response.put("message", "Mot de passe mis à jour avec succès.");

        return ResponseEntity.ok(response);
    }

}
