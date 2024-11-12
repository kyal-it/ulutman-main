package com.ulutman.repository;

import com.ulutman.model.entities.AdVersiting;
import com.ulutman.model.entities.Publish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AdVersitingRepository  extends JpaRepository<AdVersiting,Long> {
    @Query("SELECT a FROM AdVersiting a WHERE a.active = true")
    List<AdVersiting> findAllActiveAdvertisements();

    @Query("SELECT p FROM AdVersiting p WHERE p.createdAt < :expirationTime")
    List<AdVersiting> findAllByCreatedAtBefore(@Param("expirationTime") LocalDateTime expirationTime);

    @Query("SELECT a FROM AdVersiting a WHERE a.user.id = :userId AND a.active = true")
    List<AdVersiting> findAllActiveAdvertisementsByUserId(@Param("userId") Long userId);

    @Query("SELECT p FROM AdVersiting p WHERE p.active = false")
    List<AdVersiting> findAllByActiveFalse();
}
