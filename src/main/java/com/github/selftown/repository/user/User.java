package com.github.selftown.repository.user;

import com.github.selftown.repository.chat.Chat;
import com.github.selftown.repository.comment.Comment;
import com.github.selftown.repository.likes.Likes;
import com.github.selftown.repository.post.Post;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(nullable = false)
    private String nickname;

    @Column(name="porgile_image",nullable = false)
    private String profileImage;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @OneToMany(mappedBy = "user_id", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Likes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "user_id", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Chat> chatRooms = new ArrayList<>();

    @OneToMany(mappedBy = "user_id", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user_id", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Comment> comments = new ArrayList<>();


}
