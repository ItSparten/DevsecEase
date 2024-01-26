package com.cozy.services;

import com.cozy.dto.response.CustomPageResponse;
import com.cozy.dto.response.CustomPageable;
import com.cozy.entities.Agent;
import com.cozy.entities.Homeowner;
import com.cozy.entities.Student;
import com.cozy.entities.User;
import com.cozy.enumeration.Role;
import com.cozy.repositories.AgentRepository;
import com.cozy.repositories.HomeownerRepository;
import com.cozy.repositories.StudentRepository;
import com.cozy.repositories.UserRepository;
import com.cozy.services.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.springframework.data.domain.PageImpl;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private HomeownerRepository homeownerRepository;
    @Mock
    private StudentRepository studentRepository;

    @Mock
    private AgentRepository agentRepository;


    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testGetAllStudents() {
        // Créez une liste fictive d'étudiants fictifs
        List<Student> studentList = initStudents();

        // Configurez le comportement simulé du repository
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Student> page = new PageImpl<>(studentList, pageRequest, 3);
        when(studentRepository.findAll(pageRequest)).thenReturn(page);

        // Appelez la méthode à tester
        CustomPageResponse<Student> result = userService.getAllStudents(0, 10);

        // Vérifiez si la méthode a bien été appelée avec les bons paramètres
        verify(studentRepository).findAll(pageRequest);

        // Vérifiez si la réponse contient la liste des étudiants fictifs
        assertNotNull(result);
        assertEquals(studentList, result.getContent());
        assertEquals(2, result.getContent().size());

        // Vérifiez les propriétés de CustomPageable
        CustomPageable customPageable = result.getPageable();
        assertNotNull(customPageable);
        assertEquals(10, customPageable.getPageSize());
        assertEquals(0, customPageable.getPageNumber());
        assertEquals(2, customPageable.getTotalElements());
    }

    @Test
    public void testGetAllAgent() {
        // Créez une liste fictive d'agents fictifs
        List<Agent> agentList = initAgents();

        // Configurez le comportement simulé du repository
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Agent> page = new PageImpl<>(agentList, pageRequest, 2);
        when(agentRepository.findAll(pageRequest)).thenReturn(page);

        // Appelez la méthode à tester
        CustomPageResponse<Agent> result = userService.getAllAgent(0, 10);

        // Vérifiez si la méthode a bien été appelée avec les bons paramètres
        verify(agentRepository).findAll(pageRequest);

        // Vérifiez si la réponse contient la liste des agents fictifs
        assertNotNull(result);
        assertEquals(agentList, result.getContent());
        assertEquals(2, result.getContent().size());

        // Vérifiez les propriétés de CustomPageable
        CustomPageable customPageable = result.getPageable();
        assertNotNull(customPageable);
        assertEquals(10, customPageable.getPageSize());
        assertEquals(0, customPageable.getPageNumber());
        assertEquals(2, customPageable.getTotalElements());
    }

    private List<Agent> initAgents() {
        List<Agent> agentList = new ArrayList<>();

        Agent agent1 = new Agent();
        agent1.setFirstname("agent1");
        agent1.setLastname("agent1");
        agent1.setEmail("agent1@example.com");
        agent1.setPassword("password");
        agent1.setRole(Role.AGENT);
        agent1.setActive(true);
        agent1.setPhoneNumber("1234567890");

        Agent agent2 = new Agent();
        agent2.setFirstname("agent2");
        agent2.setLastname("agent2");
        agent2.setEmail("agent2@example.com");
        agent2.setPassword("password");
        agent2.setRole(Role.AGENT);
        agent2.setActive(true);
        agent2.setPhoneNumber("1234567890");


        agentList.add(agent1);
        agentList.add(agent2);

        return agentList;
    }


    @Test
    public void testGetAllHomeowner() {
        // Créez une liste fictive de propriétaires fictifs
        List<Homeowner> homeownerList = initHomeowner();

        // Configurez le comportement simulé du repository
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Homeowner> page = new PageImpl<>(homeownerList, pageRequest, 2);
        when(homeownerRepository.findAll(pageRequest)).thenReturn(page);

        // Appelez la méthode à tester
        CustomPageResponse<Homeowner> result = userService.getAllHomeowner(0, 10);


        // Vérifiez si la méthode a bien été appelée avec les bons paramètres
        verify(homeownerRepository).findAll(pageRequest);

        // Vérifiez si la réponse contient la liste des propriétaires fictifs
        assertNotNull(result);
        assertEquals(homeownerList, result.getContent());
        assertEquals(2,result.getContent().size());

        // Vérifiez les propriétés de CustomPageable
        CustomPageable customPageable = result.getPageable();
        assertNotNull(customPageable);
        assertEquals(10, customPageable.getPageSize());
        assertEquals(0, customPageable.getPageNumber());
        assertEquals(2, customPageable.getTotalElements());
    }

    @Test
    public void testActivateAccount() {
        // Créez un utilisateur fictif pour les besoins du test
        User user = new User();
        user.setId(1L);
        user.setActive(false);

        // Configurez le comportement simulé du repository
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Appelez la méthode à tester
        userService.activateAccount(1L);

        // Vérifiez si la méthode a bien été appelée avec les bons paramètres
        verify(userRepository).findById(1L);
        verify(userRepository).save(user);

        // Vérifiez si l'utilisateur est actif après activation
        assertTrue(user.isActive());
    }

    @Test
    public void testDeactivateAccount() {
        // Créez un utilisateur fictif actif pour les besoins du test
        User user = new User();
        user.setId(1L);
        user.setActive(false);

        // Configurez le comportement simulé du repository
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Appelez la méthode à tester
        userService.deactivateAccount(1L);

        // Vérifiez si la méthode a bien été appelée avec les bons paramètres
        verify(userRepository).findById(1L);
        verify(userRepository).save(user);

        // Vérifiez si l'utilisateur est désactivé après désactivation
        assertFalse(user.isActive());
    }

    private List<Student> initStudents() {
        List<Student> studentList = new ArrayList<>();

        Student student1 = new Student();
        student1.setId(1L);
        student1.setFirstname("student1");
        student1.setLastname("student1");
        student1.setEmail("student1@example.com");
        student1.setPassword("password");
        student1.setRole(Role.STUDENT);
        student1.setActive(true);
        student1.setUniversityName("University Name");
        student1.setPhoneNumber("123456789");

        Student student2 = new Student();
        student2.setId(2L);
        student2.setFirstname("student2");
        student2.setLastname("student2");
        student2.setEmail("student2@example.com");
        student2.setPassword("password");
        student2.setRole(Role.STUDENT);
        student2.setActive(true);
        student2.setUniversityName("University Name");
        student2.setPhoneNumber("123456789");

        studentList.add(student1);
        studentList.add(student2);

        return studentList;
    }
    private List<Homeowner> initHomeowner() {
        List<Homeowner> homeownerList = new ArrayList<>();
        Homeowner homeowner1 = new Homeowner();
        homeowner1.setId(1L);
        homeowner1.setFirstname("homeowner1");
        homeowner1.setLastname("homeowner1");
        homeowner1.setEmail("homeowner1@example.com");
        homeowner1.setPassword("password");
        homeowner1.setRole(Role.HOMEOWNER);
        homeowner1.setActive(true);
        homeowner1.setPhoneNumber("1234567890");
        homeowner1.setAge(30);
        homeowner1.setNationalIdentityCard("12345678");

        Homeowner homeowner2 = new Homeowner();
        homeowner2.setId(2L);
        homeowner2.setFirstname("homeowner2");
        homeowner2.setLastname("homeowner2");
        homeowner2.setEmail("homeowner2@example.com");
        homeowner2.setPassword("password");
        homeowner2.setRole(Role.HOMEOWNER);
        homeowner2.setActive(true);
        homeowner2.setPhoneNumber("1234567890");
        homeowner2.setAge(20);
        homeowner2.setNationalIdentityCard("12345678");

        homeownerList.add(homeowner1);
        homeownerList.add(homeowner2);
        return homeownerList;

    }


}
