package com.example.jo1ntask.repository;

import com.example.jo1ntask.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
