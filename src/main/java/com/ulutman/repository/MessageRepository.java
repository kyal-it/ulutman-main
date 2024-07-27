package com.ulutman.repository;

import com.ulutman.model.entities.Message;
import com.ulutman.model.enums.ModeratorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Repository
public interface MessageRepository  extends JpaRepository<Message,Long> {

    List<Message> findByUserId(Long userId);
}

