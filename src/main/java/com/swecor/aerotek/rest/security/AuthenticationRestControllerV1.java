package com.swecor.aerotek.rest.security;

import com.swecor.aerotek.model.security.Employee;
import com.swecor.aerotek.model.security.EntityParent;
import com.swecor.aerotek.model.security.User;
import com.swecor.aerotek.persist.security.EmployeeRepository;

import com.swecor.aerotek.persist.security.UserRepository;
import com.swecor.aerotek.service.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.parser.Entity;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/auth")
public class AuthenticationRestControllerV1 {

    private final AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private EmployeeRepository employeeRepository;
    //private EntityRepoParent entityRepoParent;
    private JwtTokenProvider jwtTokenProvider;

    public AuthenticationRestControllerV1(AuthenticationManager authenticationManager, UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequestDTO request) {
        try {

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            if (userRepository.findByEmail(request.getEmail()).isEmpty()){
                Employee user = employeeRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
                String token = jwtTokenProvider.createToken(request.getEmail(), user.getRole().name());
                Map<Object, Object> response = new HashMap<>();
                response.put("email", request.getEmail());
                response.put("token", token);

                return ResponseEntity.ok(response);
            }else {
                User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
                String token = jwtTokenProvider.createToken(request.getEmail(), user.getRole().name());
                Map<Object, Object> response = new HashMap<>();
                response.put("email", request.getEmail());
                response.put("token", token);

                return ResponseEntity.ok(response);
            }

        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Invalid email/password combination", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }
}
