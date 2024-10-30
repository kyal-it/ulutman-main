package com.ulutman.repository;

import com.ulutman.model.entities.Publish;
import com.ulutman.model.enums.*;
import org.springframework.data.jpa.domain.Specification;
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
    @Query("SELECT publish FROM Publish publish WHERE publish.category = 'REAL_ESTATE' AND publish.subCategory = ?1")
    List<Publish> findBySubCategoryREAL_ESTATE(Subcategory subCategory);

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


    @Query("SELECT p FROM Publish p " +
           "JOIN p.propertyDetails pd " +
           "WHERE (:minTotalArea IS NULL OR pd.totalArea >= :minTotalArea) " +
           "AND (:maxTotalArea IS NULL OR pd.totalArea <= :maxTotalArea) " +
           "AND (:minKitchenArea IS NULL OR pd.kitchenArea >= :minKitchenArea) " +
           "AND (:maxKitchenArea IS NULL OR pd.kitchenArea <= :maxKitchenArea) " +
           "AND (:minLivingArea IS NULL OR pd.livingArea >= :minLivingArea) " +
           "AND (:maxLivingArea IS NULL OR pd.livingArea <= :maxLivingArea) " +
           "AND (:minYear IS NULL OR pd.yearOfConstruction >= :minYear) " +
           "AND (:maxYear IS NULL OR pd.yearOfConstruction <= :maxYear) " +
           "AND (:transportType IS NULL OR pd.transportType = :transportType) " +
           "AND (:walkingDistance IS NULL OR pd.walkingDistance <= :walkingDistance) " +
           "AND (:transportDistance IS NULL OR pd.transportDistance <= :transportDistance)")
    List<Publish> filterPublishes(
            @Param("minTotalArea") Double minTotalArea,
            @Param("maxTotalArea") Double maxTotalArea,
            @Param("minKitchenArea") Double minKitchenArea,
            @Param("maxKitchenArea") Double maxKitchenArea,
            @Param("minLivingArea") Double minLivingArea,
            @Param("maxLivingArea") Double maxLivingArea,
            @Param("minYear") Integer minYear,
            @Param("maxYear") Integer maxYear,
            @Param("transportType") TransportType transportType,
            @Param("walkingDistance") Double walkingDistance,
            @Param("transportDistance") Double transportDistance
    );

    @Query("SELECT p FROM Publish p WHERE p.category IN :categories AND p.title IN :titles AND p.metro IN :metros")
    List<Publish> findFilteredPublishes(@Param("categories") List<Category> categories,
                                        @Param("titles") List<String> titles,
                                        @Param("metros") List<Metro> metros);

    List<Publish> findAll(Specification<Publish> specification);
}
