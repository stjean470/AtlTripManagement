package com.personal.AtlTripManagement.repository;

import com.personal.AtlTripManagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email) throws UsernameNotFoundException;
}
