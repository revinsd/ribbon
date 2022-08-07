package com.example.ribbon.service;

import com.example.ribbon.model.User;
import com.example.ribbon.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserServiceTest {

    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    UserRepository userRepository;

    @Test
    @Transactional
    void followToUser() {
    }
}