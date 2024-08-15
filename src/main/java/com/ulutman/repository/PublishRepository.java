package com.ulutman.repository;

import com.ulutman.model.entities.Publish;
import com.ulutman.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PublishRepository extends JpaRepository<Publish, Long> {

    //    @Query("SELECT p.user FROM Publish p WHERE p.id = :publishId")
//    User getUserByPublishId(@Param("publishId") Long publishId);

    // Запрос для получения всех публикаций пользователя по userId
    @Query("SELECT p FROM Publish p WHERE p.user.id = :userId")
    List<Publish> findAllByUserId(@Param("userId") Long userId);

    @Query("""
            SELECT publish FROM Publish publish WHERE 
            (:descriptions IS NULL OR LOWER(publish.description) IN (:descriptions)) 
            AND (:categories IS NULL OR publish.category IN (:categories)) 
             AND (:createDate IS NULL OR publish.createDate IN (:createDate))           
            AND (:publishStatuses IS NULL OR LOWER(publish.publishStatus) IN (:publishStatuses))
            """)
    List<Publish> publishFilter(@Param("descriptions") List<String> descriptions,
                                @Param("categories") List<String> categories,
                                @Param("createDate") List<LocalDate> createDate,
                                @Param("publishStatuses") List<String> publishStatuses);
}
