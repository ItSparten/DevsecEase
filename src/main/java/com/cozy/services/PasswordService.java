package com.cozy.services;

import com.cozy.entities.User;
import jakarta.mail.MessagingException;

public interface PasswordService {
    User findByEmail(String email);

    void resetPassword(String email) throws MessagingException;
    boolean checkCurrentPassword(Long userId, String currentPassword);

    void updatePassword(Long userId, String newPassword);
}
