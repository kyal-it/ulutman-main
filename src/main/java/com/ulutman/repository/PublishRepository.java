package com.ulutman.repository;

import com.ulutman.model.entities.Publish;
import com.ulutman.model.enums.Category;
import com.ulutman.model.enums.CategoryStatus;
import com.ulutman.model.enums.PublishStatus;
import org.springframework.cache.annotation.Cacheable;
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

    @Query("SELECT COUNT(p) FROM Publish p WHERE p.user.id = :userId")
    @Cacheable("false")
        // Отключаем кэширование для этого запроса
    Integer countPublicationsByUserId(@Param("userId") Long userId);

    @Query("SELECT p FROM Publish p WHERE " +
           "(:title IS NULL OR :title = '' OR LOWER(p.title) LIKE LOWER(CONCAT(:title, '%')))")
    List<Publish> findProductsByTitle(@Param("title") String title);

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
            @Param("createDates") List<LocalDate> createDates
    );

    @Query("SELECT p FROM Publish p WHERE p.user.id = :userId AND LOWER(p.user.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Publish> filterPublicationsByUserIdAndUserName(@Param("userId") Long userId, @Param("name") String name);

//    @Query("SELECT p FROM Publish p WHERE p.user.id = :userId AND p.user.name LIKE %:name%")
//    @Cacheable("false")
//    List<Publish> filterPublicationsByUserIdAndUserName(@Param("userId") Long userId, @Param("name") String name);

    @Query("SELECT p FROM Publish p WHERE " +
           "(:minCount IS NULL OR (SELECT COUNT(p2) FROM Publish p2 WHERE p2.category = p.category) >= :minCount) " +
           "AND (:maxCount IS NULL OR (SELECT COUNT(p2) FROM Publish p2 WHERE p2.category = p.category) <= :maxCount)")
    List<Publish> findProductsByPublicationCount(@Param("minCount") Integer minCount, @Param("maxCount") Integer maxCount);

    @Query("SELECT publish FROM Publish publish WHERE publish.category=('WORK')")
    List<Publish> findByCategoryWork();

    @Query("SELECT publish FROM Publish publish WHERE publish.category=('RENT')")
    List<Publish> findByCategoryRent();

    @Query("SELECT publish FROM Publish publish WHERE publish.category=('SELL')")
    List<Publish> findByCategorySell();

    @Query("SELECT publish FROM Publish publish WHERE publish.category=('HOTEL')")
    List<Publish> findByCategoryHotel();

    @Query("SELECT publish FROM Publish publish WHERE publish.category =('AUTO')")
    List<Publish> findByCategoryServices();

    @Query("SELECT publish FROM Publish publish WHERE publish.category=('REAL_ESTATE')")
    List<Publish> findByCategoryRealEstate();
}
