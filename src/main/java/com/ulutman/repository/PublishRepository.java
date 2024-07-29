package com.ulutman.repository;

import com.ulutman.model.entities.Publish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PublishRepository extends JpaRepository<Publish, Long> {
    @Query("""
                 SELECT p FROM Publish p WHERE 
                 (:descriptions IS NULL OR LOWER(p.description)  IN :descriptions) AND
                 (:categories IS NULL OR LOWER( p.category) IN :categories) AND
                 (:publishStatuses IS NULL OR LOWER(p.publishStatus)  IN :publishStatuses) AND
                 (:createDates IS NULL OR p.createDate IN :createDates)
            """)
    List<Publish> publishFilter(
            @Param("descriptions") List<String> descriptions,
            @Param("categories") List<String> categories,
            @Param("createDates") List<LocalDate> createDates,
            @Param("publishStatuses") List<String> publishStatuses);
}
