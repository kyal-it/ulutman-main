package com.ulutman.repository;

import com.ulutman.model.entities.User;
import com.ulutman.model.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserAccontRepository extends JpaRepository<UserAccount, Long> {

    @Query("SELECT user FROM UserAccount user WHERE user.gmail = :email")
    Optional<UserAccount> findByEmail(@Param("email") String email);

}
