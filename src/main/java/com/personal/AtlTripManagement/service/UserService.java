package com.personal.AtlTripManagement.service;

import com.personal.AtlTripManagement.model.AuthProvider;
import com.personal.AtlTripManagement.model.User;
import com.personal.AtlTripManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
            if (!passwordEncoder.matches(loginUser.getPassword(), user.getPassword())) {
                throw new RuntimeException("Password does not match");
            }
            return loginUser;
        }
        throw new RuntimeException("User not found");
    }


}
