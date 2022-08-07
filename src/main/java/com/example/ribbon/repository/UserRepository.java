package com.example.ribbon.repository;

import com.example.ribbon.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    User findUserByUsername(String username);
    User findUserByPhotoFileName(String photoPath);
}
