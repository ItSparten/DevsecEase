package com.cozy.services.impl;

import com.cozy.dto.request.ContactUsRequest;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactUsService {
    private final EmailService emailService;
    public void processContactForm(ContactUsRequest contactUsRequest) throws MessagingException {
      String message=  "<html><body>" +
                "<p><strong>Nom:</strong> " + contactUsRequest.getName() + "</p>" +
                "<p><strong>E-mail:</strong> " + contactUsRequest.getEmail() + "</p>" +
                "<p><strong>Message:</strong> " + contactUsRequest.getMessage() + "</p>" +
                "</body></html>";
        emailService.sendEmail("easemakers.cosy@gmail.com","Nouveau message de contact",message);
    }

}
