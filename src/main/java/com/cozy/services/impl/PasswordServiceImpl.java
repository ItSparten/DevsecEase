package com.cozy.services.impl;

import com.cozy.entities.User;
import com.cozy.repositories.UserRepository;
import com.cozy.services.PasswordService;
import com.cozy.util.PasswordGenerator;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).get();
    }

    @Override
    public void resetPassword(String email) throws MessagingException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé pour l'email: " + email));
        String newPassword = PasswordGenerator.generateRandomPassword();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        sendResetPasswordEmail(user.getEmail(), newPassword);
    }



    @Override
    public boolean checkCurrentPassword(Long userId, String currentPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé pour l'ID: " + userId));
        return passwordEncoder.matches(currentPassword, user.getPassword());
    }

    @Override
    public void updatePassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé pour l'ID: " + userId));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    private void sendResetPasswordEmail(String email, String newPassword) throws MessagingException {
        this.emailService.sendEmail(email,"Réinitialisation du mot de passe","Votre nouveau mot de passe est: " + newPassword);
    }
}
