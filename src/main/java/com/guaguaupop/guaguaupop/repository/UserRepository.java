package com.guaguaupop.guaguaupop.repository;

import com.guaguaupop.guaguaupop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional <User> findByUsername(String username);
    User findByEmail(@Param(("email")) String email);
    User findByPhone(@Param(("phone")) Integer phone);
}

