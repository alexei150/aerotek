package com.swecor.aerotek.service.security;


import com.swecor.aerotek.model.security.User;
import com.swecor.aerotek.rest.security.UserRequestDTO;
import com.swecor.aerotek.rest.exceptions.library.EmailIsAbsent;
import org.springframework.data.jdbc.repository.query.Query;

import java.util.List;

public interface UserService {

    void createUser(UserRequestDTO user);

    void deleteUser(String email) throws EmailIsAbsent;

    void updateUser(UserRequestDTO user);

    List<User> showUsers();

    User getUser(String email);


}
