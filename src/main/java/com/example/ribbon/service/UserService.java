package com.example.ribbon.service;

import com.example.ribbon.model.Post;
import com.example.ribbon.model.Role;
import com.example.ribbon.model.User;
import com.example.ribbon.repository.PostRepository;
import com.example.ribbon.repository.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.JSqlParserUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Transactional
@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @PersistenceContext
    EntityManager entityManager;
    @Value("${upload.path}")
    String uploadPath;


    public boolean saveUser(User user){
        if(userRepository.findUserByUsername(user.getUsername())!=null) return false;
        user.setRoles(Collections.singleton(new Role(2L,"USER")));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRegistrationDate(new Date());
        userRepository.save(user);
        return true;
    }

    public void setNewPassword(User user, String password){
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
    }

    public void changeOneRoleToAnother(User user, Role roleToChange, Role wantedRole){
        Set<Role> userRoles=user.getRoles();
        userRoles.forEach(e->{
            if(e.equals(roleToChange)) e=wantedRole;
        });
        userRepository.save(user);

    }
    public void addRole(User user, Role newRole){
        Set<Role> userRoles=user.getRoles();
        userRoles.add(newRole);
        userRepository.save(user);
    }

    public List<Post> getAllUserPosts(User user){
        return postRepository.findAllByAuthor(user);
    }

    public long getUserRating(User user){
        long rating=0;
        for(Post post: getAllUserPosts(user)){
            rating+=post.getRating();
        }
        return rating;
    }

    public String savePhotoWithNewName(MultipartFile file){
        Random random = new Random();
        String fileName;
        do{
            fileName=Math.abs(random.nextInt())+file.getOriginalFilename().replace(";","");
        }while(userRepository.findUserByPhotoFileName(fileName)!=null);
        File newFile = new File(uploadPath+fileName);
        try {
            file.transferTo(newFile);
        }catch (IOException e){
            e.printStackTrace();
        }
        return fileName;
    }

    public void deleteUserPhotoIfExists(User user){
        if (user.getPhotoFileName()!=null){
            File file = new File(uploadPath + user.getPhotoFileName());
            file.delete();
            user.setPhotoFileName(null);
        }
    }


    public void followToUser(User userToFollow, User newFollower){
        newFollower=userRepository.findUserByUsername(newFollower.getUsername());
        newFollower.getFollowing().add(userToFollow);
    }

    public void unfollowUser(User userToUnfollow, User follower){
        follower=userRepository.findUserByUsername(follower.getUsername());
        follower.getFollowing().remove(userToUnfollow);
    }


    public boolean isUserIsFollowingUser(User user, User ownerOfProfile){
        user=userRepository.findUserByUsername(user.getUsername());
        return user.getFollowing().contains(ownerOfProfile);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user==null) throw new UsernameNotFoundException("User not found");
        return user;
    }
}
