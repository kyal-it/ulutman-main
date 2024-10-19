package com.ulutman.repository;

import com.ulutman.model.entities.MyPublish;
import com.ulutman.model.entities.Publish;
import com.ulutman.model.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyPublishRepository extends JpaRepository<MyPublish,Long> {

    boolean existsByUserAccountAndPublish(UserAccount userAccount, Publish publish);


    void deleteByPublishId(Long publishId);
}
