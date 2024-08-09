package com.github.selftown.domain.likes.domain;

import com.github.selftown.domain.post.domain.Post;
import com.github.selftown.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

}
