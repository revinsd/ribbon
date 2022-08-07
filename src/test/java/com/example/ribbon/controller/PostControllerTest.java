package com.example.ribbon.controller;

import com.example.ribbon.model.Post;
import com.example.ribbon.repository.PostRepository;
import com.example.ribbon.repository.UserRepository;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PostControllerTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;

    @Test
    void createNewPost() {
        Date currentDate = new Date();
        Post post = new Post();
        post.setText("12321");
        post.setTitle("asdg");
        post.setCreationDate(currentDate);
        post.setAuthor(userRepository.findUserByUsername("revinsd"));
        postRepository.save(post);
    }
}