package com.ulutman.repository;

import com.ulutman.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    @Query("SELECT user FROM User user WHERE user.email=:email")
    Optional<User> findByEmail(@Param("email") String email);

    @Query("""
                 SELECT user FROM User user WHERE 
                 user.name IN :name 
                 OR user.role IN :role 
                 OR user.createDate IN :createDate 
                 OR user.status IN :status
            """)
    List<User> userFilter(@Param("name") List<String> names,
                          @Param("role") List<String> roles,
                          @Param("createDate") List<LocalDate> createDate,
                          @Param("status") List<String> status);
}
