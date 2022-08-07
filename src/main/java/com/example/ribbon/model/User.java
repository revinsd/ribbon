package com.example.ribbon.model;

import com.example.ribbon.service.UserService;
import org.hibernate.FetchNotFoundException;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Proxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.*;
@Entity
@Table(name="t_user")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Size(min=5, message = "Не менее 5 символов")
    private String username;
    @Size(min=8, message = "Не менее 8 символов")
    private String password;
    @Transient
    @Size(min=8, message = "Не менее 8 символов")
    private String passwordConfirmation;
    @Transient
    @Size(min=8, message = "Не менее 8 символов")
    private String newPassword;
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name="user_role",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Set<Role> roles = new HashSet<>();
    private Date registrationDate;
    private String about;
    private String photoFileName;
    @ManyToMany
    @JoinTable(name="followers",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="follower_id"))
    private Set<User> followers = new HashSet<>();
    @ManyToMany
    @JoinTable(name="followers",
            joinColumns = @JoinColumn(name="follower_id"),
            inverseJoinColumns = @JoinColumn(name="user_id"))
    private Set<User> following = new HashSet<>();
    @OneToMany(mappedBy = "user")
    private Set<PostUser> ratedPosts = new HashSet<>();


    public User() {}

    public long getId() {return id;}

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public Set<Role> getRoles() {return roles;}
    public void setRoles(Set<Role> roles) {this.roles = roles;}

    public String getPasswordConfirmation() {return passwordConfirmation;}
    public void setPasswordConfirmation(String passwordConfirmation) {this.passwordConfirmation = passwordConfirmation;}

    public Date getRegistrationDate() {return registrationDate;}
    public void setRegistrationDate(Date registrationDate) {this.registrationDate = registrationDate;}

    public String getAbout() {return about;}
    public void setAbout(String about) {this.about = about;}

    public String getPhotoFileName() {return photoFileName;}
    public void setPhotoFileName(String photoPath) {this.photoFileName = photoPath;}

    public Set<User> getFollowers() {return followers;}
    public void setFollowers(Set<User> followers) {this.followers = followers;}

    public Set<User> getFollowing() {return following;}
    public void setFollowing(Set<User> following) {this.following = following;}

    public Set<PostUser> getRatedPosts() {return ratedPosts;}
    public void setRatedPosts(Set<PostUser> ratedPosts) {this.ratedPosts = ratedPosts;}

    public String getNewPassword() {return newPassword;}
    public void setNewPassword(String newPassword) {this.newPassword = newPassword;}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id==user.id && Objects.equals(username,user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,username);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
