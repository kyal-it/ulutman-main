package com.ulutman.repository;

import com.ulutman.model.entities.TelegramUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TelegramUserRepository  extends JpaRepository<TelegramUser, Long> {
    Optional<TelegramUser> findByChatId(String chatId);
}