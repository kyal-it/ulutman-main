package com.ulutman.repository;

import com.ulutman.model.entities.Mailing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailingRepository extends JpaRepository<Mailing,Long> {
}
