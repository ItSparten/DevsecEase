package com.cozy.services.impl;

import com.cozy.dto.request.AuthenticationRequest;
import com.cozy.dto.request.RegisterRequest;
import com.cozy.dto.request.VerifyOtpRequest;
import com.cozy.dto.response.AuthenticationResponse;
import com.cozy.entities.Agent;
import com.cozy.entities.Homeowner;
import com.cozy.entities.Student;
import com.cozy.entities.User;
import com.cozy.exceptions.AccountDisabledException;
import com.cozy.exceptions.EmailAlreadyExistsException;
import com.cozy.exceptions.InvalidOtpException;
import com.cozy.exceptions.NotFoundException;
import com.cozy.repositories.AgentRepository;
import com.cozy.repositories.HomeownerRepository;
import com.cozy.repositories.StudentRepository;
import com.cozy.repositories.UserRepository;
import com.cozy.security.JwtService;
import com.cozy.services.AuthenticationService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final HomeownerRepository homeownerRepository;
    private final AgentRepository agentRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    @Override
    public String register(RegisterRequest request) throws MessagingException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
        // Générer un code OTP
        String otpCode = generateOTP();
        User user;
        switch (request.getRole()) {
            case HOMEOWNER:
                user = new Homeowner(
                        request.getFirstname(),
                        request.getLastname(),
                        request.getEmail(),
                        passwordEncoder.encode(request.getPassword()),
                        request.getRole(),
                        request.getPhoneNumber(),
                        request.getAge(),
                        request.getNationalIdentityCard()
                );
                user.setActive(false); // Marquer l'utilisateur comme inactif jusqu'à la vérification de l'e-mail
                user.setOtpCode(otpCode); // Stocker le code OTP dans l'entité User
                homeownerRepository.save((Homeowner) user);
                emailService.sendVerificationEmail(user.getEmail(), otpCode);
                break;

            case AGENT:
                user = new Agent(
                        request.getFirstname(),
                        request.getLastname(),
                        request.getEmail(),
                        passwordEncoder.encode(request.getPassword()),
                        request.getRole(),
                        request.getPhoneNumber()
                );
                user.setActive(true);
                agentRepository.save((Agent) user);
                break;

            case STUDENT:
                user = new Student(
                        request.getFirstname(),
                        request.getLastname(),
                        request.getEmail(),
                        passwordEncoder.encode(request.getPassword()),
                        request.getRole(),
                        request.getPhoneNumber(),
                        request.getUniversityName()
                );
                user.setActive(false); // Marquer l'utilisateur comme inactif jusqu'à la vérification de l'e-mail
                user.setOtpCode(otpCode); // Stocker le code OTP dans l'entité User
                studentRepository.save((Student) user);
                emailService.sendVerificationEmail(user.getEmail(), otpCode);
                break;

            default:
                throw new IllegalArgumentException("Invalid user role.");
        }

        return "Registration successful. Check your email for the verification code.";
    }


    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(createAuthenticationToken(request));
        } catch (BadCredentialsException e) {
            throw new NotFoundException("Invalid email or password, please try again");
        }

        User user = findUserByEmail(request.getEmail());
        if (!user.isActive()) {
            throw new AccountDisabledException("Account is disabled. Please contact support.");
        }
        String jwtToken = jwtService.generateToken(user);


        return createAuthenticationResponse(jwtToken, user.getRole().name(), user.getId());
    }

    @Override
    public String verifyOtp(VerifyOtpRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable"));

        if (request.getOtpCode().equals(user.getOtpCode())) {
            user.setActive(true);
            userRepository.save(user);
            return "La vérification de l'e-mail a réussi. Vous pouvez maintenant vous connecter.";
        }else {
            throw new InvalidOtpException("Code invalide. Veuillez réessayer.");
        }
    }
    private UsernamePasswordAuthenticationToken createAuthenticationToken(AuthenticationRequest request) {
        return new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
    }

    private AuthenticationResponse createAuthenticationResponse(String jwtToken, String role, Long userId) {
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .role(role)
                .userId(userId)
                .build();
    }

    private String generateOTP() {
        // Générer un code OTP aléatoire de 4 chiffres
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000);

        return String.valueOf(otp);
    }
}
