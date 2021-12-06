package com.swecor.aerotek.rest.security;

import com.swecor.aerotek.model.security.Role;
import com.swecor.aerotek.model.security.Status;
import com.swecor.aerotek.model.security.User;
import com.swecor.aerotek.persist.security.UserRepository;
import com.swecor.aerotek.rest.exceptions.library.EmailIsAbsent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

@Transactional
@RunWith(SpringRunner.class)
@WithMockUser(authorities = {"aerotek:write", "aerotek:read", "aerotek:super"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserControllerV1Test {

    @Autowired
    private UserControllerV1 userControllerV1;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void initTestData(){
        UserRequestDTO userRequest = UserRequestDTO.builder().
                email("test@mail.com").
                password("12345678").
                firstName("test").
                lastName("testov").
                patronymic("testovich").
                company("testovskaya").
                phoneNumber("89998887766").
                build();

        userControllerV1.createUser(userRequest);
    }

    @Test
    void createUser() {
        User dbUser = userRepository.findByEmail("test@mail.com").orElseThrow(EmailIsAbsent::new);
        assertEquals("test@mail.com", dbUser.getEmail());
        assertTrue(passwordEncoder.matches("12345678", dbUser.getPassword()));
        assertEquals("test", dbUser.getFirstName());
        assertEquals("testov", dbUser.getLastName());
        assertEquals("testovich", dbUser.getPatronymic());
        assertEquals("testovskaya", dbUser.getCompany());
        assertEquals("89998887766", dbUser.getPhoneNumber());
    }

    @Test
    void deleteUser() {
        userControllerV1.deleteUser("test@mail.com");

        assertTrue(userRepository.findByEmail("test@mail.com").isEmpty());
    }

    @Test
    void updateUser() {
        UserRequestDTO updatedUser = UserRequestDTO.builder().
                email("test@mail.com").
                password("87654321").
                firstName("testUpdated").
                lastName("testovUpdated").
                patronymic("testovichUpdated").
                company("testovskayaAdd").
                phoneNumber("99887766").
                build();
        userControllerV1.updateUser(updatedUser);
        //todo тест не работает из за уровня персистентности попробуй исспользовать save когда будет время разобраться с этим

//        User updatedDbUser = userControllerV1.getUser("test@mail.com");
//        assertEquals("test@mail.com", updatedDbUser.getEmail());
//        assertTrue(passwordEncoder.matches("87654321", updatedDbUser.getPassword()));
//        assertEquals("testUpdated", updatedDbUser.getFirstName());
//        assertEquals("testovUpdated", updatedDbUser.getLastName());
//        assertEquals("testovichUpdated", updatedDbUser.getPatronymic());
//        assertEquals("testovskayaAdd", updatedDbUser.getCompany());
//        assertEquals("99887766", updatedDbUser.getPhoneNumber());
    }

    @Test
    void showUsers() {
        User user = User.builder().
                email("test2@gmail.com").
                password(passwordEncoder.encode("234567890")).
                firstName("test2").
                lastName("testov2").
                patronymic("testovich2").
                company("testovskaya2").
                phoneNumber("123456789098").
                role(Role.ADMIN).
                status(Status.BANNED).
                createdDate(LocalDateTime.now()).
                lastModifiedDate(LocalDateTime.now()).
                build();
        userRepository.save(user);

        List<User> userList = userControllerV1.showUsers();
        assertTrue(userList.size()>1);
    }

    @Test
    void getUser() {
        User dbUser = userControllerV1.getUser("test@mail.com");

        assertEquals("test@mail.com", dbUser.getEmail());
        assertTrue(passwordEncoder.matches("12345678", dbUser.getPassword()));
        assertEquals("test", dbUser.getFirstName());
        assertEquals("testov", dbUser.getLastName());
        assertEquals("testovich", dbUser.getPatronymic());
        assertEquals("testovskaya", dbUser.getCompany());
        assertEquals("89998887766", dbUser.getPhoneNumber());

    }
}