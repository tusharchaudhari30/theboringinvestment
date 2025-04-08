package com.theboringproject.authenticationservice.repository;

import com.theboringproject.authenticationservice.model.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String email);
}
