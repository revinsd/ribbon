package com.example.ribbon.model;

import com.sun.istack.NotNull;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NonNull
    private Date creationDate;
    @NotNull
    private String text;
    @NotNull
    private String title;

    private long rating;
    @NotNull
    @ManyToOne(cascade = CascadeType.MERGE)
    private User author;
    @OneToMany(mappedBy = "post")
    private Set<PostUser> ratedBy=new HashSet<>();
    @Transient
    private boolean isLikedByCurrentUser;
    @Transient
    private boolean isDislikedByCurrentUser;

    public Post() {}

    public long getId() {return id;}

    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    public String getText() {return text;}
    public void setText(String text) {this.text = text;}

    public long getRating() {return rating;}
    public void setRating(long rating) {this.rating = rating;}

    public Date getCreationDate() {return creationDate;}
    public void setCreationDate(Date creationDate) {this.creationDate = creationDate;}

    public User getAuthor() {return author;}
    public void setAuthor(User author) {this.author = author;}

    public Set<PostUser> getRatedBy() {return ratedBy;}

    public void setRatedBy(Set<PostUser> ratedBy) {this.ratedBy = ratedBy;}

    public boolean isLikedByCurrentUser() {return isLikedByCurrentUser;}
    public void setLikedByCurrentUser(boolean likedByCurrentUser) {isLikedByCurrentUser = likedByCurrentUser;}

    public boolean isDislikedByCurrentUser() {return isDislikedByCurrentUser;}

    public void setDislikedByCurrentUser(boolean dislikedByCurrentUser) {isDislikedByCurrentUser = dislikedByCurrentUser;}
}
