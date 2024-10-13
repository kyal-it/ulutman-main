package com.ulutman.repository;

import com.ulutman.model.entities.Comment;
import com.ulutman.model.enums.ModeratorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByUserId(Long postId);

    List<Comment> findByUserName(String userName);

    @Query("""
        SELECT comment FROM Comment comment WHERE 
        (:content IS NULL OR :content = '' OR LOWER(comment.content) LIKE LOWER(CONCAT('%', :content, '%')))
    """)
    List<Comment> commentsFilterByContents(@Param("content") String content);

    @Query("SELECT c FROM Comment c WHERE " +
           "(:createDates IS NULL OR c.createDate IN :createDates) AND " +
           "(:moderatorStatuses IS NULL OR c.moderatorStatus IN :moderatorStatuses)")
    List<Comment> findCommentsByFilters(
            @Param("createDates") List<LocalDate> createDates,
            @Param("moderatorStatuses") List<ModeratorStatus> moderatorStatuses);
}
