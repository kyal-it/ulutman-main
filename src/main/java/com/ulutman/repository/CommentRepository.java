package com.ulutman.repository;

import com.ulutman.model.entities.Comment;
import com.ulutman.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByUserId(Long postId);

    @Query("SELECT c FROM Comment c WHERE " +
           "(:users IS NULL OR c.user.id = :users) AND " +
           "(:content IS NULL OR LOWER(c.content)  IN :content) AND " +
           "(:createDates IS NULL OR c.createDate IN :createDates) AND " +
           "(:moderatorStatuses IS NULL OR LOWER(c.moderatorStatus)  IN :moderatorStatuses)")
    List<Comment> findCommentsByFilters(
            @Param("users") List<User> users,
            @Param("content") List<String> content,
            @Param("createDates") List<LocalDate> createDates,
            @Param("moderatorStatuses") List<String> moderatorStatuses);
}
