package com.personal.AtlTripManagement.service;

import com.personal.AtlTripManagement.model.AuthProvider;
import com.personal.AtlTripManagement.model.User;
import com.personal.AtlTripManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User is not in the database");
        }
        return new UserPrincipal(user);
    }

    public User registerLocal(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setAuthProvider(AuthProvider.LOCAL);
        return userRepository.save(user);
    }

    public User loginUser(User user) {
        User loginUser = userRepository.findByEmail(user.getEmail());
        if(loginUser != null) {
            if (!passwordEncoder.matches(user.getPassword(), loginUser.getPassword())) {
                throw new RuntimeException("Password does not match");
            }
            return loginUser;
        }
        throw new RuntimeException("User not found");
    }

    public User authenticateUser(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return userRepository.findByEmail(email);
        }catch (AuthenticationException ae) {
            throw new RuntimeException("Invalid User name or password");
        }
    }


}
