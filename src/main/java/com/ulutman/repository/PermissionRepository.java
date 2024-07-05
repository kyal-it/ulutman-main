package com.ulutman.repository;

import com.ulutman.model.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Repository
public interface PermissionRepository  extends JpaRepository<Permission,Long> {
}
