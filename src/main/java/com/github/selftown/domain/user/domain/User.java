package com.github.selftown.domain.user.domain;

import com.github.selftown.domain.chat.domain.ChatRoom;
import com.github.selftown.domain.comment.domain.Comment;
import com.github.selftown.domain.likes.domain.Likes;
import com.github.selftown.domain.post.domain.Post;
import com.github.selftown.global.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    public enum Role {
        GUEST, MEMBER, ADMIN
    }

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(nullable = false)
    private String nickname;

    @Column(name="profile_image",nullable = false)
    private String profileImage;

    @Column(name = "kakao_id", nullable = false, unique = true)  // 카카오 로그인 이메일 필드 추가
    private Long kakaoId;

    @Column(name = "email", nullable = false, unique = true)  // 카카오 로그인 이메일 필드 추가
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role = Role.GUEST;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Likes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<ChatRoom> chatRooms = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Comment> comments = new ArrayList<>();

    public void upgradeToMember() {
        this.role = Role.MEMBER;
    }

    public void assignAdmin() {
        this.role = Role.ADMIN;
    }
}
