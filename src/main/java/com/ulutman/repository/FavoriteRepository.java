package com.ulutman.repository;

import com.ulutman.model.entities.Favorite;
import com.ulutman.model.entities.Publish;
import com.ulutman.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    @Query("SELECT favorite FROM Favorite favorite WHERE favorite.user.id = :id")
    Favorite getFavoritesByUserId(@Param("id") Long id);

    @Query("SELECT p FROM Publish p JOIN p.favorites f WHERE f.id = :id")
    List<Publish> findProductsInFavorites(@Param("id") Long favoritesId);

    @Query("SELECT COUNT(f) FROM Favorite f JOIN f.publishes p WHERE p.id = :publishId")
    Long countByPublishId(@Param("publishId") Long publishId);

    // В вашем репозитории
    Optional<Favorite> findByUser(User user);

}
