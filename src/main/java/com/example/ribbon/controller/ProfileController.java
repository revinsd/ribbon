package com.example.ribbon.controller;

import com.example.ribbon.model.Post;
import com.example.ribbon.model.User;
import com.example.ribbon.repository.PostRepository;
import com.example.ribbon.repository.UserRepository;
import com.example.ribbon.service.PostService;
import com.example.ribbon.service.UserService;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ProfileController {
    @Autowired
    UserService userService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostService postService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/profile")
    public String profile(Model model,@AuthenticationPrincipal User user){
        List<Post> allUserPosts= userService.getAllUserPosts(user);
        List<Post> lastUserPostArray = postRepository.findFirstByAuthorOrderByCreationDateDesc(user);
        postService.setIsRatedByUserAttribute(lastUserPostArray,user);
        Post lastUserPost = null;
        if(lastUserPostArray.size()>0) lastUserPost = lastUserPostArray.get(0);
        user = userRepository.findUserByUsername(user.getUsername());
        model.addAttribute("userPosts", allUserPosts)
                .addAttribute("userPostsCount", allUserPosts.size())
                .addAttribute("rating",userService.getUserRating(user))
                .addAttribute("title","Профиль")
                .addAttribute("followersCount",user.getFollowers().size())
                .addAttribute("post", lastUserPost)
                .addAttribute("isProfileOwner", true);
        return "profile";
    }

    @GetMapping("/profile/edit")
    public String profileEdit(Model model,@AuthenticationPrincipal User user){
        List<Post> allUserPosts= userService.getAllUserPosts(user);
        user = userRepository.findUserByUsername(user.getUsername());
        model.addAttribute("userPosts", allUserPosts)
                .addAttribute("userPostsCount", allUserPosts.size())
                .addAttribute("followersCount",user.getFollowers().size())
                .addAttribute("rating",userService.getUserRating(user))
                .addAttribute("title","Изменение профиля")
                .addAttribute("newUserParams", new User());
        return "profileEdit";
    }

    @PostMapping("/profile/edit")
    public String profileEditSubmit(Model model,@AuthenticationPrincipal User user, @RequestParam MultipartFile photo, @RequestParam String about){
        if(!photo.getOriginalFilename().trim().equals("")){
            userService.deleteUserPhotoIfExists(user);
            user.setPhotoFileName(userService.savePhotoWithNewName(photo));

        }
        user.setAbout(about);
        userRepository.save(user);
        return "redirect:/profile";
    }

    @GetMapping("/user/{username}")
    public String userProfile(Model model, @AuthenticationPrincipal User user, @PathVariable String username){
        User ownerOfProfile = userRepository.findUserByUsername(username);
        model.addAttribute("isProfileOwner",ownerOfProfile.equals(user));
        List<Post> allUserPosts= userService.getAllUserPosts(ownerOfProfile);
        List<Post> lastUserPostArray = postRepository.findFirstByAuthorOrderByCreationDateDesc(ownerOfProfile);
        Post lastUserPost=null;
        if(user!=null) postService.setIsRatedByUserAttribute(allUserPosts,user);
        if(lastUserPostArray.size()>0) lastUserPost = lastUserPostArray.get(0);
        model.addAttribute("user", ownerOfProfile)
                .addAttribute("userPosts", allUserPosts)
                .addAttribute("userPostsCount", allUserPosts.size())
                .addAttribute("rating",userService.getUserRating(ownerOfProfile))
                .addAttribute("followersCount",ownerOfProfile.getFollowers().size())
                .addAttribute("title",ownerOfProfile.getUsername())
                .addAttribute("isFollowing",userService.isUserIsFollowingUser(user,ownerOfProfile))
                .addAttribute("post",lastUserPost);

        return "profile";
    }
    @GetMapping("/user/{username}/follow")
    public String followUser(Model model, @AuthenticationPrincipal User user, @PathVariable String username){
        User ownerOfProfile = userRepository.findUserByUsername(username);
        if(!userService.isUserIsFollowingUser(user,ownerOfProfile)) userService.followToUser(ownerOfProfile, user);
        return "redirect:/user/"+username;
    }

    @GetMapping("/user/{username}/unfollow")
    public String unfollowUser(Model model, @AuthenticationPrincipal User user, @PathVariable String username){
        User ownerOfProfile = userRepository.findUserByUsername(username);
        if(userService.isUserIsFollowingUser(user,ownerOfProfile)) userService.unfollowUser(ownerOfProfile,user);
        return "redirect:/user/"+username;
    }

    @GetMapping("/user/{username}/posts")
    public String allUserPosts(Model model, @AuthenticationPrincipal User user, @PathVariable String username){
        User ownerOfProfile = userRepository.findUserByUsername(username);
        List<Post> allUserPosts= userService.getAllUserPosts(ownerOfProfile);
        if(user!=null) postService.setIsRatedByUserAttribute(allUserPosts,user);
        ownerOfProfile=userRepository.findUserByUsername(ownerOfProfile.getUsername());
        model.addAttribute("posts",allUserPosts)
                .addAttribute("user", ownerOfProfile)
                .addAttribute("followersCount",ownerOfProfile.getFollowers().size())
                .addAttribute("title",ownerOfProfile.getUsername())
                .addAttribute("userPostsCount",allUserPosts.size())
                .addAttribute("rating",userService.getUserRating(ownerOfProfile));
        return "profileAllUserPosts";
    }

    @PostMapping("/profile/changePassword")
    public String changePassword(Model model,
                                 @AuthenticationPrincipal User user,
                                 @ModelAttribute("newUserParams") @Valid User newUserParams,
                                 BindingResult bindingResult){
        List<Post> allUserPosts= userService.getAllUserPosts(user);
        user=userRepository.findUserByUsername(user.getUsername());
        model.addAttribute("userPosts", allUserPosts)
                .addAttribute("userPostsCount", allUserPosts.size())
                .addAttribute("followersCount",user.getFollowers().size())
                .addAttribute("rating",userService.getUserRating(user))
                .addAttribute("title", "Изменение профиля");
        if(bindingResult.hasErrors()){
            model.addAttribute("error", "Ошибка");
            return "profileEdit";
        }
        if(!bCryptPasswordEncoder.matches(newUserParams.getPassword(),user.getPassword())){
            model.addAttribute("error","Неверный текущий пароль");
            return "profileEdit";
        }
        if(!newUserParams.getNewPassword().equals(newUserParams.getPasswordConfirmation())){
            model.addAttribute("error", "Пароли не совпадают");
            return "profileEdit";
        }
        userService.setNewPassword(user,newUserParams.getNewPassword());
        model.addAttribute("success", "Пароль успешно изменен");
        return "profileEdit";
    }


}
