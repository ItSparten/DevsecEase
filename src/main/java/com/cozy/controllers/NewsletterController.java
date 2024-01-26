package com.cozy.controllers;

import com.cozy.entities.Newsletter;
import com.cozy.services.NewsletterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/newsletter")
@RequiredArgsConstructor
public class NewsletterController {

    private final NewsletterService newsletterService;

    @PostMapping("/subscribe/{email}")
    public ResponseEntity<String> subscribeToNewsletter(@PathVariable String email) {
        Newsletter subscribedUser = newsletterService.saveSubscription(email);
        return ResponseEntity.ok("Inscrit avec succès avec l'adresse e-mail : " + subscribedUser.getEmail());
    }
    @GetMapping("/subscribers")
    public ResponseEntity<List<Newsletter>> getAllSubscribers() {
        List<Newsletter> subscribers = newsletterService.getAllSubscribers();
        return ResponseEntity.ok(subscribers);
    }

}
