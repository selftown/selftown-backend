package com.github.selftown.domain.likes.domain;

import com.github.selftown.domain.post.domain.Post;
import com.github.selftown.domain.user.domain.User;
import com.github.selftown.global.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Likes extends BaseEntity {
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

}
