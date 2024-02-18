package com.cozy.startup;

import com.cozy.dto.request.UniversityRequest;
import com.cozy.entities.Agent;
import com.cozy.entities.Homeowner;
import com.cozy.entities.Student;
import com.cozy.entities.User;
import com.cozy.enumeration.Role;
import com.cozy.repositories.AgentRepository;
import com.cozy.repositories.HomeownerRepository;
import com.cozy.repositories.StudentRepository;
import com.cozy.repositories.UserRepository;
import com.cozy.services.UniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FakeDataCommandLineRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final HomeownerRepository homeownerRepository;
    private final AgentRepository agentRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final UniversityService universityService;

    @Override
    public void run(String... args) throws Exception {
        if(userRepository.findAll().isEmpty()){
            initAdmin();
            initHomeowner();
            initAgent();
            initStudent();
            initUnversity();
        }


    }

    private void initAdmin() {
        User admin = new User();
        admin.setFirstname("Admin");
        admin.setLastname("Admin");
        admin.setEmail("admin@gmail.com");
        admin.setPassword(passwordEncoder.encode("password"));
        admin.setRole(Role.ADMIN);
        admin.setActive(true);
        userRepository.save(admin);

    }

    private void initUnversity() {
        var esprit = UniversityRequest.builder()
                .universityName("ESPRIT")
                .location("Z.I Chotrana II - B.P. 160 Pôle Technologique 2083 Cite El Ghazala Raoued")
                .build();
        universityService.createUniversity(esprit);
        var ult = UniversityRequest.builder()
                .universityName("ULT")
                .location("32 Bis, 1002 Av. Kheireddine Pacha, Tunis 1002")
                .build();
        universityService.createUniversity(ult);

        var sesame = UniversityRequest.builder()
                .universityName("SESAME")
                .location("ZI Chotrana I BP4 Parc Technologique El Ghazela, Ariana 2088")
                .build();
        universityService.createUniversity(sesame);

        var tekup = UniversityRequest.builder()
                .universityName("TEK-UP")
                .location("08 Rue Newton, Ariana 2088")
                .build();
        universityService.createUniversity(tekup);
    }


    private void initHomeowner() {
        Homeowner homeowner1 = new Homeowner();
        homeowner1.setFirstname("Bilel");
        homeowner1.setLastname("Oueslati");
        homeowner1.setEmail("oueslatibilel55@gmail.com ");
        homeowner1.setPassword(passwordEncoder.encode("password"));
        homeowner1.setRole(Role.HOMEOWNER);
        homeowner1.setActive(true);
        homeowner1.setPhoneNumber("1234567890");
        homeowner1.setAge(30);
        homeowner1.setNationalIdentityCard("12345678");

        homeownerRepository.save(homeowner1);
    }
    private void initAgent() {
        Agent agent1 = new Agent();
        agent1.setFirstname("Ali");
        agent1.setLastname("Bouzidi");
        agent1.setEmail("agent1@gmail.com");
        agent1.setPassword(passwordEncoder.encode("password"));
        agent1.setRole(Role.AGENT);
        agent1.setActive(true);
        agent1.setPhoneNumber("1234567890");

        agentRepository.save(agent1);

    }
    private void initStudent() {
        Student student1 = new Student();
        student1.setFirstname("Bassem");
        student1.setLastname("Raouafi");
        student1.setEmail("bassamraouafi@gmail.com");
        student1.setPassword(passwordEncoder.encode("password"));
        student1.setRole(Role.STUDENT);
        student1.setActive(true);
        student1.setUniversityName("ULT");
        student1.setPhoneNumber("1234567890");
        studentRepository.save(student1);

    }
}

