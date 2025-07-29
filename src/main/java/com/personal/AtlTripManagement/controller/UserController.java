package com.personal.AtlTripManagement.controller;

import com.personal.AtlTripManagement.model.User;
import com.personal.AtlTripManagement.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userService.registerLocal(user);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("user", Map.of(
                    "id", registeredUser.getId(),
                    "email", registeredUser.getEmail(),
                    "firstName", registeredUser.getFirstname(),
                    "lastName", registeredUser.getLastname()
            ));
            return ResponseEntity.ok(response);

        }catch (RuntimeException re) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", re.getMessage());
            return ResponseEntity.badRequest().body(error);
        }

    }

    @PostMapping("/login/local")
    public ResponseEntity<?> loginUser(@RequestBody User user, HttpServletRequest request) {
        try {
            User loggedInUser = userService.loginUser(user);
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loggedInUser.getEmail(), loggedInUser.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            HttpSession session = request.getSession(true);
            session.setAttribute("userId", loggedInUser.getId());
            session.setAttribute("userEmail", loggedInUser.getEmail());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "User registered successfully");
            response.put("user", Map.of(
                    "id", loggedInUser.getId(),
                    "email", loggedInUser.getEmail(),
                    "firstName", loggedInUser.getFirstname(),
                    "lastName", loggedInUser.getLastname()
            ));
            response.put("sessionId", session.getId());
            return ResponseEntity.ok(response);

        }catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null){
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        response.sendRedirect("http://localhost:5173/login");
        return ResponseEntity.ok().build();
    }






}
