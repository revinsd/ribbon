package com.example.ribbon.controller;

import com.example.ribbon.model.Post;
import com.example.ribbon.model.User;
import com.example.ribbon.model.ajaxModel.PostAjaxRequest;
import com.example.ribbon.model.ajaxModel.RatingAjaxRequest;
import com.example.ribbon.model.ajaxModel.RatingAjaxResponse;
import com.example.ribbon.repository.PostRepository;
import com.example.ribbon.repository.RoleRepository;
import com.example.ribbon.repository.UserRepository;
import com.example.ribbon.service.PostService;
import com.example.ribbon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
public class PostController {
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserService userService;
    @Autowired
    PostService postService;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/newPost")
    public String newPost(Model model){
        model.addAttribute("title","Новый пост");
        model.addAttribute("newPost",new Post());
        return "newPost";
    }

    @PostMapping("/newPost")
    public String createNewPost(Model model,@ModelAttribute("newPost") Post post,@AuthenticationPrincipal User user){
        Date currentDate = new Date();
        post.setCreationDate(currentDate);
        post.setAuthor(user);
        postRepository.save(post);
        return "redirect:/newPosts";
    }

    @GetMapping("/newPosts")
    public String newPosts(Model model,@AuthenticationPrincipal User user){
        model.addAttribute("title","Новое");
        List<Post> posts=postRepository.findAllByOrderByCreationDateDesc();
        if(user!=null) postService.setIsRatedByUserAttribute(posts,user);
        model.addAttribute("posts",posts);
        return "posts";
    }
    @GetMapping("/bestPosts")
    public String bestPosts(Model model,@AuthenticationPrincipal User user){
        model.addAttribute("title","Лучшее");
        List<Post> posts=postRepository.findAllByOrderByRatingDesc();
        if(user!=null) postService.setIsRatedByUserAttribute(posts,user);
        model.addAttribute("posts", posts);
        return "posts";
    }

    @GetMapping("/followingPosts")
    public String followingPosts(Model model,@AuthenticationPrincipal User user){
        model.addAttribute("title","Мои подписки");
        List<Post> followingPosts=new ArrayList<>();
        user=userRepository.findUserByUsername(user.getUsername());
        Set<User> following = user.getFollowing();
        following.forEach(e->{
            followingPosts.addAll(postRepository.findAllByAuthor(e));
        });
        postService.setIsRatedByUserAttribute(followingPosts,user);
        model.addAttribute("posts", followingPosts);
        return "posts";
    }

    @PostMapping("/changePostRating")
    public @ResponseBody RatingAjaxResponse changePostRating(@RequestBody RatingAjaxRequest ratingAjaxRequest, @AuthenticationPrincipal User user){
        long postId=ratingAjaxRequest.getPostId();
        String operationType=ratingAjaxRequest.getOperationType();
        Post post = postRepository.findById(postId).orElseThrow();
        RatingAjaxResponse ratingAjaxResponse = new RatingAjaxResponse();
        ratingAjaxResponse.setPostRating(postService.changePostRating(post,operationType,user));
        return ratingAjaxResponse;
    }

    @PostMapping("/deletePost")
    public @ResponseBody PostAjaxRequest deletePost(@AuthenticationPrincipal User user, @RequestBody PostAjaxRequest postAjaxRequest){
        Post post = postRepository.findById(postAjaxRequest.getPostId()).orElseThrow();

        postService.deletePost(post,user);
        return postAjaxRequest;
    }
}
