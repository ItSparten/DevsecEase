package com.cozy.services;

import com.cozy.dto.request.AuthenticationRequest;
import com.cozy.dto.request.RegisterRequest;
import com.cozy.dto.response.AuthenticationResponse;
import com.cozy.entities.Agent;
import com.cozy.entities.Homeowner;
import com.cozy.entities.Student;
import com.cozy.entities.User;
import com.cozy.enumeration.Role;
import com.cozy.exceptions.AccountDisabledException;
import com.cozy.exceptions.NotFoundException;
import com.cozy.repositories.AgentRepository;
import com.cozy.repositories.HomeownerRepository;
import com.cozy.repositories.StudentRepository;
import com.cozy.repositories.UserRepository;
import com.cozy.security.JwtService;
import com.cozy.services.impl.AuthenticationServiceImpl;
import jakarta.mail.MessagingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private HomeownerRepository homeownerRepository;
    @Mock
    private AgentRepository agentRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;


    @Test
    public void testRegisterHomeowner() throws MessagingException {
        // Créez une instance de RegisterRequest pour le rôle de HOMEOWNER
        RegisterRequest request = createHomeownerRegisterRequest();
        // Configurez le comportement simulé de userRepository.existsByEmail
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        // Appelez la méthode à tester
        String result = authenticationService.register(request);
        // Vérifiez si la méthode renvoie la bonne valeur
        assertEquals("Registration successful", result);
        // Assurez-vous que le repository approprié (homeownerRepository) a été utilisé pour enregistrer l'utilisateur
        verify(homeownerRepository).save(any(Homeowner.class));
    }
    @Test
    public void testRegisterStudent() throws MessagingException {
        // Créez une instance de RegisterRequest pour le rôle de STUDENT
        RegisterRequest request = createStudentRegisterRequest();
        // Configurez le comportement simulé de userRepository.existsByEmail
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        // Appelez la méthode à tester
        String result = authenticationService.register(request);
        // Vérifiez si la méthode renvoie la bonne valeur
        assertEquals("Registration successful", result);
        // Assurez-vous que le repository approprié (studentRepository) a été utilisé pour enregistrer l'utilisateur
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    public void testRegisterAgent() throws MessagingException {
        // Créez une instance de RegisterRequest pour le rôle de AGENT
        RegisterRequest request = createAgentRegisterRequest();
        // Configurez le comportement simulé de userRepository.existsByEmail
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        // Appelez la méthode à tester
        String result = authenticationService.register(request);
        // Vérifiez si la méthode renvoie la bonne valeur
        assertEquals("Registration successful", result);
        // Assurez-vous que le repository approprié (agentRepository) a été utilisé pour enregistrer l'utilisateur
        verify(agentRepository).save(any(Agent.class));
    }




    @Test
    public void testAuthenticate_WithValidCredentialsAndActiveAccount() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest("test@example.com", "password");
        User user = new Homeowner("Homeowner", "Homeowner", "test@example.com", "encodedPassword", Role.HOMEOWNER, "1234567890", 30, "12345678");
        user.setActive(true);

        // Mock authenticationManager behavior
        Authentication authentication = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        // Mock userRepository behavior
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));

        // Mock jwtService behavior
        when(jwtService.generateToken(user)).thenReturn("jwtToken");

        // Act
        AuthenticationResponse response = authenticationService.authenticate(request);

        // Assert
        assertNotNull(response);
        assertEquals("jwtToken", response.getToken());
        assertEquals(Role.HOMEOWNER.name(), response.getRole());
    }

    @Test(expected = NotFoundException.class)
    public void testAuthenticateWithInvalidCredentials() {
        // Configuration du comportement simulé de l'AuthenticationManager
        doThrow(new NotFoundException("Invalid email or password, please try again")).when(authenticationManager)
                .authenticate(any());

        // Appel de la méthode à tester avec des identifiants non valides
        AuthenticationRequest request = new AuthenticationRequest("test@example.com", "wrongpassword");

        // La méthode doit lancer une NotFoundException, ce qui sera vérifié par l'annotation @Test
        authenticationService.authenticate(request);

        // Assurez-vous que l'AuthenticationManager a été appelé avec les bons paramètres
        verify(authenticationManager).authenticate(any());
    }
    @Test(expected = AccountDisabledException.class)
    public void testInactiveAccount() {
        // Créez un utilisateur fictif inactif
        User inactiveUser = new User();
        inactiveUser.setEmail("inactive@example.com");
        inactiveUser.setPassword("password");
        inactiveUser.setActive(false);

        // Configurez le comportement simulé du repository pour retourner cet utilisateur fictif
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(inactiveUser));

        // Appelez la méthode d'authentification avec cet utilisateur fictif
        AuthenticationRequest request = new AuthenticationRequest("inactive@example.com", "password");
        // La méthode doit lancer une AccountDisabledException, ce qui sera vérifié par l'annotation @Test
        authenticationService.authenticate(request);

        // L'exception AccountDisabledException doit être levée
    }

    private RegisterRequest createHomeownerRegisterRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setRole(Role.HOMEOWNER);
        request.setFirstname("John");
        request.setLastname("Doe");
        request.setEmail("john.doe@example.com");
        request.setPassword("password");
        request.setPhoneNumber("1234567890");
        request.setAge(30);
        request.setNationalIdentityCard("12345678");
        return request;
    }

    private RegisterRequest createStudentRegisterRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setRole(Role.STUDENT);
        request.setFirstname("student");
        request.setLastname("student");
        request.setEmail("student@example.com");
        request.setPassword("password");
        request.setUniversityName("Univ name");
        request.setPhoneNumber("1234567890");
        return request;
    }

    private RegisterRequest createAgentRegisterRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setRole(Role.AGENT);
        request.setFirstname("agent");
        request.setLastname("agent");
        request.setEmail("agent@example.com");
        request.setPassword("password");
        request.setPhoneNumber("1234567890");
        return request;
    }
}
