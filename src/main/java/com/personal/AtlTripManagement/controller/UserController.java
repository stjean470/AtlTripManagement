package com.personal.AtlTripManagement.controller;

import com.personal.AtlTripManagement.model.User;
import com.personal.AtlTripManagement.service.UserPrincipal;
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
    public ResponseEntity<?> loginUser(@RequestBody User user, HttpServletRequest request, HttpServletResponse responseBody) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            User authUser = userPrincipal.getUser();
            HttpSession session = request.getSession(true);
            session.setAttribute("userId", authUser.getId());
            session.setAttribute("userEmail", authUser.getEmail());

            responseBody.setHeader("Access-Control-Allow-Credentials", "true");
            responseBody.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");


            Map<String, Object> response = new HashMap<>();
            response.put("message", "User logged in successfully");
            response.put("user", Map.of(
                    "id", authUser.getId(),
                    "email", authUser.getEmail(),
                    "firstName", authUser.getFirstname(),
                    "lastName", authUser.getLastname()
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

    @GetMapping("/authorize")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("authenticated", true);
            response.put("userId", session.getAttribute("userId"));
            response.put("userEmail", session.getAttribute("userEmail"));
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("authenticated", false, "error", "No active session"));
    }






}
