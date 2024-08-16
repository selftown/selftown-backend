package com.github.selftown.domain.user.entity;

//import com.github.selftown.domain.chat.domain.ChatRoom;
//import com.github.selftown.domain.comment.domain.Comment;
//import com.github.selftown.domain.likes.domain.Likes;
//import com.github.selftown.domain.post.domain.Post;
import com.github.selftown.domain.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;




@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String email;

    @NotNull
    private String profileImage;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Role role;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auth_info_id")
    private AuthInfo authInfo;

//    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
//    private List<Likes> likes = new ArrayList<>();
//
//    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
//    private List<ChatRoom> chatRooms = new ArrayList<>();
//
//    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
//    private List<Post> posts = new ArrayList<>();
//
//    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
//    private List<Comment> comments = new ArrayList<>();

}
