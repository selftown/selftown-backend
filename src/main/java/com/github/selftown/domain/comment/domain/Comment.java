package com.github.selftown.domain.comment.domain;

import com.github.selftown.domain.post.domain.Post;
import com.github.selftown.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
@Entity
@Data
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;
}
