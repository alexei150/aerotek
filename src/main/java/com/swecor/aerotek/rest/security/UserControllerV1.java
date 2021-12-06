package com.swecor.aerotek.rest.security;

import com.swecor.aerotek.model.security.User;
import com.swecor.aerotek.service.security.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/user")
public class UserControllerV1 {

    private final UserService userService;

    public UserControllerV1(@Qualifier("userServiceImpl") UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public void createUser(@RequestBody @Valid UserRequestDTO userRequestDTO){
            userService.createUser(userRequestDTO);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('aerotek:super')")
    public void deleteUser(String email) {
        userService.deleteUser(email);
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('aerotek:read')")
    public void updateUser(@Valid @RequestBody UserRequestDTO userRequestDTO){
        userService.updateUser(userRequestDTO);
    }

    @GetMapping("/show")
    @PreAuthorize("hasAuthority('aerotek:super')")
    public List<User> showUsers(){
        return userService.showUsers();
    }

    @GetMapping("/get")
    @PreAuthorize("hasAuthority('aerotek:read')")
    public User getUser(String email){
        return userService.getUser(email);
    }
}
