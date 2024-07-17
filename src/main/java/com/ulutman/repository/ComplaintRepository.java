package com.ulutman.repository;

import com.ulutman.model.entities.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Repository
public interface ComplaintRepository  extends JpaRepository<Complaint,Long> {

}
