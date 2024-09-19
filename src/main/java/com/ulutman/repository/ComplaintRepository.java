package com.ulutman.repository;

import com.ulutman.model.entities.Complaint;
import com.ulutman.model.enums.ComplaintStatus;
import com.ulutman.model.enums.ComplaintType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

@Query("SELECT c FROM Complaint c WHERE " +
       "(:complaintTypes IS NULL OR c.complaintType IN :complaintTypes) AND " +
       "(:createDates IS NULL OR c.createDate IN :createDates) AND " +
       "(:complaintStatuses IS NULL OR c.complaintStatus IN :complaintStatuses)")
List<Complaint> complaintFilter(
        @Param("complaintTypes") List<ComplaintType> complaintTypes,
        @Param("createDates") List<LocalDate> createDates,
        @Param("complaintStatuses") List<ComplaintStatus> complaintStatuses
);
}

