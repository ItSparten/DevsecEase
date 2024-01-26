package com.cozy.services;

import com.cozy.entities.Newsletter;

import java.util.List;

public interface NewsletterService {
    Newsletter saveSubscription(String email);
    List<Newsletter> getAllSubscribers();
}
