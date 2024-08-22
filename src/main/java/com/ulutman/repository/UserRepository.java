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
              (:names IS NULL OR LOWER(user.name) LIKE (:names))
            AND (:roles IS NULL OR user.role IN (:roles))
            AND (:createDate IS NULL OR user.createDate IN (:createDate))
            AND (:statuses IS NULL OR LOWER(user.status) IN (:statuses))
             """)
    List<User> userFilter(@Param("names") List<String> names,
                          @Param("roles") List<String> roles,
                          @Param("createDate") List<LocalDate> createDate,
                          @Param("statuses") List<String> statuses);


//    @Query("""
//    SELECT user FROM User user WHERE
//    (:names IS NULL OR LOWER(user.name) LIKE LOWER(CONCAT(:names, '%')))
//    AND (:roles IS NULL OR user.role IN (:roles))
//    AND (:createDate IS NULL OR user.createDate IN (:createDate))
//    AND (:statuses IS NULL OR LOWER(user.status) IN (:statuses))
//    """)
//    List<User> userFilter(@Param("names") String names,
//                          @Param("roles") List<String> roles,
//                          @Param("createDate") List<LocalDate> createDate,
//                          @Param("statuses") List<String> statuses);


}