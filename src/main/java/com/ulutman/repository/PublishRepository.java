package com.ulutman.repository;

import com.ulutman.model.entities.Publish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
@Service
public interface PublishRepository extends JpaRepository<Publish, Long> {

}
