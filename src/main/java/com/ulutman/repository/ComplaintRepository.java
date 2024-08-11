package com.ulutman.repository;

import com.ulutman.model.entities.Complaint;
import com.ulutman.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    @Query("SELECT c FROM Complaint c WHERE " +
           "(:users IS NULL OR  c.user=:users) AND" +
           "(:complaintTypes IS NULL OR c.complaintType = :complaintTypes) AND " +
           "(:createDates IS NULL OR c.createDate = :createDates) AND " +
           "(:complaintStatuses IS NULL OR c.complaintStatus = :complaintStatuses)")
    List<Complaint> complaintFilter(
            @Param("users") List<User> users,
            @Param("complaintTypes") List<String> complaintTypes,
            @Param("createDates") List<LocalDate> createDates,
            @Param("complaintStatuses") List<String> complaintStatuses
    );
}

