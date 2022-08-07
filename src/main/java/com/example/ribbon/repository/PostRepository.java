package com.example.ribbon.repository;

import com.example.ribbon.model.Post;
import com.example.ribbon.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {
    List<Post> findAllByOrderByRatingDesc();
    List<Post> findAllByOrderByCreationDateDesc();
    List<Post> findAllByAuthor(User user);
    List<Post> findFirstByAuthorOrderByCreationDateDesc(User user);
}
