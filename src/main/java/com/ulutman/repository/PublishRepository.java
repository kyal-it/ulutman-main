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

    @Query("SELECT p FROM Publish p WHERE LOWER(p.title) LIKE LOWER(CONCAT( :title, '%'))")
    List<Publish> filterPublishesByTitle(@Param("title") String title);

    @Query("SELECT p FROM Publish p WHERE (:categories IS NULL OR p.category IN :categories)")
    List<Publish> filterPublishesByCategory(@Param("categories") List<Category> categories);

    @Query("SELECT p FROM Publish p WHERE p.publishStatus IN :publishStatuses")
    List<Publish>  filterPublishesByStatus(@Param("publishStatuses") List<PublishStatus> publishStatuses);

    @Query("SELECT p FROM Publish p WHERE (:createDates IS NULL OR p.createDate IN :createDates)")
    List<Publish> filterPublishesByCreateDate(@Param("createDates") List<LocalDate> createDates);


    @Query("SELECT p FROM Publish p WHERE (:names IS NULL OR LOWER(p.user.name) LIKE LOWER(CONCAT(:names, '%')))")
    List<Publish> filterPublishesByUserName(@Param("names") String names);


    @Query("SELECT p FROM Publish p WHERE (:categoryStatus IS NULL OR p.categoryStatus IN :categoryStatus)")
    List<Publish> filterPublishesByCategoryStatus(@Param("categoryStatus") List<CategoryStatus> categoryStatus);


    @Query("SELECT COUNT(p) FROM Publish p WHERE p.user.id = :userId")
    Integer countPublicationsByUserId(@Param("userId") Long userId);

    @Query("SELECT p FROM Publish p WHERE p.user.id = :userId")
    List<Publish> filterPublishesByUser(@Param("userId") Long userId);

    @Query("SELECT COUNT(p) FROM Publish p WHERE p.user.id = :userId")
    long countByUser(@Param("userId") Long userId);

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

    @Query("SELECT p FROM Publish p WHERE p.categoryStatus = :categoryStatus")
    List<Publish> findByCategoryStatus(@Param("categoryStatus") CategoryStatus categoryStatus);

    @Query("SELECT p FROM Publish p WHERE (:categories IS NULL OR p.category IN :categories) " +
           "ORDER BY " +
           "CASE WHEN :sortBy = 'newest' THEN p.createDate END DESC, " +
           "CASE WHEN :sortBy = 'cheapest' THEN p.price END ASC, " +
           "CASE WHEN :sortBy = 'expensive' THEN p.price END DESC")
    List<Publish> filterPublishesByCategory(
            @Param("categories") List<Category> categories,
            @Param("sortBy") String sortBy
    );
}
