package com.github.selftown.domain.post.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PostResponse {
    private Long postId;
    private String title;
    private String content;
    private String category;
    private String userName;
    private String userProfileImage;
    private Long userId;
    private LocalDateTime created_at;
    private List<String> imageUrls;
}
