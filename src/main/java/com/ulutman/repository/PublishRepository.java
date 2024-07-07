package com.ulutman.repository;

import com.ulutman.model.entities.Publish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Repository
public interface PublishRepository extends JpaRepository<Publish, Long> {
    @Query("""
                 SELECT p FROM Publish p WHERE 
                 (:descriptions IS NULL OR p.description IN :descriptions) AND
                 (:categories IS NULL OR p.category IN :categories) AND
                 (:publishStatuses IS NULL OR p.publishStatus IN :publishStatuses) AND
                 (:createDates IS NULL OR p.createDate IN :createDates)
            """)
    List<Publish> publishFilter(
            @Param("descriptions") List<String> descriptions,
            @Param("categories") List<String> categories,
            @Param("createDates") List<LocalDate> createDates,
            @Param("publishStatuses") List<String> publishStatuses);


}
