package com.cozy.services.impl;

import com.cozy.entities.Newsletter;
import com.cozy.repositories.NewsletterRepository;
import com.cozy.services.NewsletterService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsletterServiceImpl implements NewsletterService {

    private final NewsletterRepository newsletterRepository;

    @Override
    public Newsletter saveSubscription(String email) {
        Newsletter newsletter = new Newsletter();
        newsletter.setEmail(email);
        return newsletterRepository.save(newsletter);
    }
    @Override
    public List<Newsletter> getAllSubscribers() {
        return newsletterRepository.findAll();
    }
}


