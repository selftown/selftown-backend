package com.github.selftown.domain.comment.domain;

import com.github.selftown.domain.post.domain.Post;
import com.github.selftown.domain.user.domain.User;
import com.github.selftown.global.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
@Entity
@Data
public class Comment extends BaseEntity {
    @Column(nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;
}
