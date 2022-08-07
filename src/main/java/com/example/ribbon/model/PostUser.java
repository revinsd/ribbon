package com.example.ribbon.model;

import javax.persistence.*;

@Entity
@Table(name="post_user")
public class PostUser {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;
    private String operationType;

    public PostUser() {}

    public PostUser(User user, Post post, String operationType) {
        this.user = user;
        this.post = post;
        this.operationType = operationType;
    }



    public long getId() {return id;}
    public void setId(long id) {this.id = id;}

    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}

    public Post getPost() {return post;}
    public void setPost(Post post) {this.post = post;}

    public String getOperationType() {return operationType;}
    public void setOperationType(String operationType) {this.operationType = operationType;}
}
