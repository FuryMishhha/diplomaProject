package com.example.backend.Repository;

import com.example.backend.Entity.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;

@Qualifier("baseDataSource")
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
