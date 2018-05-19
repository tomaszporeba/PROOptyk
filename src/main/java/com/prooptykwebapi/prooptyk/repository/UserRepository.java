package com.prooptykwebapi.prooptyk.repository;

import com.prooptykwebapi.prooptyk.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
