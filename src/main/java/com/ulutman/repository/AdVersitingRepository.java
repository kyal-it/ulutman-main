package com.ulutman.repository;

import com.ulutman.model.entities.AdVersiting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdVersitingRepository  extends JpaRepository<AdVersiting,Long> {
}
