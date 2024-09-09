package com.ulutman.repository;

import com.ulutman.model.entities.User;
import com.ulutman.model.enums.Role;
import com.ulutman.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT user FROM User user WHERE user.email=:email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("""
            SELECT user FROM User user WHERE
            (:names IS NULL OR LOWER(user.name) LIKE LOWER(CONCAT(:names, '%')))
            """)
    List<User> userFilterByName(@Param("names") String names);

    @Query("""
            SELECT user FROM User user WHERE
            (:roles IS NULL OR user.role IN :roles) AND
            (:statuses IS NULL OR user.status IN :statuses) AND
            (:createDates IS NULL OR user.createDate IN :createDates)
            """)
    List<User> userFilter(@Param("roles") List<Role> roles,
                          @Param("createDates") List<LocalDate> createDates,
                          @Param("statuses") List<Status> statuses);
}