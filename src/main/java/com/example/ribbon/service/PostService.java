package com.example.ribbon.service;

import com.example.ribbon.model.Post;
import com.example.ribbon.model.PostUser;
import com.example.ribbon.model.Role;
import com.example.ribbon.model.User;
import com.example.ribbon.repository.PostRepository;
import com.example.ribbon.repository.PostUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class PostService {
    @Autowired
    PostRepository postRepository;
    @Autowired
    PostUserRepository postUserRepository;
    public long changePostRating(Post post, String operationType, User user){
        if (user==null) return post.getRating();
        PostUser postUser = postUserRepository.findPostUserByUserAndPost(user,post);
        if(postUser==null){
            if(operationType.equals("+")) post.setRating(post.getRating()+1);
            else if(operationType.equals("-")) post.setRating(post.getRating()-1);
            postUserRepository.save(new PostUser(user,post,operationType));
        }else{
            if(postUser.getOperationType().equals("+")){
                if(operationType.equals("+")){
                    post.setRating(post.getRating()-1);
                    postUserRepository.delete(postUser);
                }else if(operationType.equals("-")){
                    post.setRating(post.getRating()-2);
                    postUser.setOperationType("-");
                    postUserRepository.save(postUser);
                }
            }else if(postUser.getOperationType().equals("-")){
                if(operationType.equals("+")){
                    post.setRating(post.getRating()+2);
                    postUser.setOperationType("+");
                    postUserRepository.save(postUser);
                }else if(operationType.equals("-")){
                    post.setRating(post.getRating()+1);
                    postUserRepository.delete(postUser);
                }
            }
        }
        postRepository.save(post);
        return post.getRating();
    }

    public void setIsRatedByUserAttribute(List<Post> posts, User user){
        posts.forEach(e->{
            PostUser postUser;
            if((postUser=postUserRepository.findPostUserByUserAndPost(user,e))!=null){
                if(postUser.getOperationType().equals("+")) e.setLikedByCurrentUser(true);
                if(postUser.getOperationType().equals("-")) e.setDislikedByCurrentUser(true);
            }
        });
    }

    public void deletePost(Post post, User user){
        boolean hasPermission=false;
        for (Role role: user.getRoles()){
            if(role.getName().equals("ADMIN")|| role.getName().equals("MODERATOR")) hasPermission=true;
        }
        if(post.getAuthor().equals(user)||hasPermission){
            PostUser postUser= postUserRepository.findPostUserByPost(post);
            if(postUser!=null) postUserRepository.delete(postUser);
            postRepository.delete(post);
        }
    }
}
