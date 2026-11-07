package com.github.rodiond26.demo_1.repository;

import com.github.rodiond26.demo_1.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
