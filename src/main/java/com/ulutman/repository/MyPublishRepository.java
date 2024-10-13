package com.ulutman.repository;

import com.ulutman.model.entities.MyPublish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyPublishRepository extends JpaRepository<MyPublish,Long> {
}
