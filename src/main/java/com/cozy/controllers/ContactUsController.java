package com.cozy.controllers;

import com.cozy.dto.request.ContactUsRequest;
import com.cozy.dto.response.SuccessMessageResponse;
import com.cozy.services.impl.ContactUsService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contact")
@RequiredArgsConstructor
public class ContactUsController {
    private final ContactUsService contactUsService;
    @PostMapping("/send")
    public ResponseEntity<SuccessMessageResponse> submitContactForm(@RequestBody ContactUsRequest contactUsRequest) throws MessagingException {
        contactUsService.processContactForm(contactUsRequest);
        return ResponseEntity.ok(new SuccessMessageResponse("Votre message a été envoyé avec succès!"));
    }
}
