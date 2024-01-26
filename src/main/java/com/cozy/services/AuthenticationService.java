package com.cozy.services;

import com.cozy.dto.request.AuthenticationRequest;
import com.cozy.dto.request.RegisterRequest;
import com.cozy.dto.request.VerifyOtpRequest;
import com.cozy.dto.response.AuthenticationResponse;
import jakarta.mail.MessagingException;

public interface AuthenticationService {
     String register(RegisterRequest request) throws MessagingException;
     AuthenticationResponse authenticate(AuthenticationRequest request);

     String verifyOtp(VerifyOtpRequest request);
}
