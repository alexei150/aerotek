package com.swecor.aerotek.service.security.impl;

import com.swecor.aerotek.model.security.Role;
import com.swecor.aerotek.model.security.Status;
import com.swecor.aerotek.model.security.User;
import com.swecor.aerotek.persist.security.UserRepository;
import com.swecor.aerotek.rest.exceptions.library.EmailIsNotUnique;
import com.swecor.aerotek.rest.security.UserRequestDTO;
import com.swecor.aerotek.rest.exceptions.library.EmailIsAbsent;
import com.swecor.aerotek.service.security.UserService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void createUser(UserRequestDTO requestUser) {
        try {
            userRepository.save(buildUser(requestUser));
        } catch (DataIntegrityViolationException e) {
            throw new EmailIsNotUnique();
        }
    }

    @Override
    public void deleteUser(String email) {
        userRepository.findByEmail(email).orElseThrow(EmailIsAbsent::new);

        userRepository.deleteByEmail(email);
    }

    @Override
    public void updateUser(UserRequestDTO requestUser) {
        userRepository.findByEmail(requestUser.getEmail()).orElseThrow(EmailIsAbsent::new);
        userRepository.updateUser(passwordEncoder.encode(requestUser.getPassword()),
                requestUser.getFirstName(),
                requestUser.getLastName(),
                requestUser.getPatronymic(),
                requestUser.getCompany(),
                requestUser.getPhoneNumber(),
                LocalDateTime.now(),
                requestUser.getEmail());
    }

    @Override
    public List<User> showUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(EmailIsAbsent::new);
    }

    private User buildUser(UserRequestDTO userRequest) {
        return User.builder().
                email(userRequest.getEmail()).
                password(passwordEncoder.encode(userRequest.getPassword())).
                firstName(userRequest.getFirstName()).
                lastName(userRequest.getLastName()).
                patronymic(userRequest.getPatronymic()).
                company(userRequest.getCompany()).
                phoneNumber(userRequest.getPhoneNumber()).
                role(Role.USER).
                status(Status.ACTIVE).
                createdDate(LocalDateTime.now()).
                lastModifiedDate(LocalDateTime.now()).
                //supervisorId(4).//////////////////////////////////////////////////////
                build();
    }




}
