package com.example.ribbon.repository;

import com.example.ribbon.model.Post;
import com.example.ribbon.model.PostUser;
import com.example.ribbon.model.User;
import org.springframework.data.jpa.repository.JpaRepository;



public interface PostUserRepository extends JpaRepository<PostUser,Long> {
    PostUser findPostUserByUserAndPost(User user, Post post);
    PostUser findPostUserByPost(Post post);

}
