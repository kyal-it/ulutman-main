package com.ulutman.repository;

import com.ulutman.model.entities.Favorite;
import com.ulutman.model.entities.Publish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    @Query("SELECT favorite FROM Favorite favorite WHERE favorite.user.id = :id")
    Favorite getFavoritesByUserId(@Param("id") Long id);

    @Query("SELECT p FROM Publish p JOIN p.favorites f WHERE f.id = :id")
    List<Publish> findProductsInFavorites(@Param("id") Long favoritesId);

}
