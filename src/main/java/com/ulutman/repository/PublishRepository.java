package com.ulutman.repository;

import com.ulutman.model.entities.Publish;
import com.ulutman.model.enums.Category;
import com.ulutman.model.enums.CategoryStatus;
import com.ulutman.model.enums.PublishStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PublishRepository extends JpaRepository<Publish, Long> {

    @Query("SELECT p FROM Publish p WHERE p.user.id = :userId")
    List<Publish> findAllByUserId(@Param("userId") Long userId);

    @Query("SELECT p FROM Publish p WHERE p.user.id = :userId")
    List<Publish> findByUserId(@Param("userId") Long userId);

    @Query("""
            SELECT publish FROM Publish publish WHERE
            (:categories IS NULL OR publish.category IN :categories) AND
            (:categoryStatuses IS NULL OR publish.categoryStatus IN :categoryStatuses)
            """)
    List<Publish> categoryFilter(@Param("categories") List<Category> categories,
                                 @Param("categoryStatuses") List<CategoryStatus> categoryStatuses);

    @Query("""
            SELECT publish FROM Publish publish WHERE
            (:categories IS NULL OR publish.category IN :categories) AND
            (:publishStatuses IS NULL OR publish.publishStatus IN :publishStatuses) AND
            (:createDates IS NULL OR publish.createDate IN :createDates)
            """)
    List<Publish> filterPublishes(
            @Param("categories") List<Category> categories,
            @Param("publishStatuses") List<PublishStatus> publishStatuses,
            @Param("createDates")List<LocalDate> createDates
    );
}
