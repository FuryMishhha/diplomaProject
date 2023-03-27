package com.example.backend.Repository;

import com.example.backend.Entity.Role;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;

@Qualifier("baseDataSource")
public interface RoleRepository extends JpaRepository<Role, Long> {
}
