package com.theboringproject.portfolio_service.repository;

import com.theboringproject.portfolio_service.model.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String email);
}
