package com.ulutman.repository;

import com.ulutman.model.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Repository
public interface MessageRepository  extends JpaRepository<Message,Long> {

    List<Message> findByUserId(Long userId);
}
