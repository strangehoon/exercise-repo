package com.whatever_market.app.user.model.repository;

import com.whatever_market.app.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

