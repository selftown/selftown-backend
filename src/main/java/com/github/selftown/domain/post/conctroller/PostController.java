package com.github.selftown.domain.post.conctroller;

import com.github.selftown.domain.post.repository.PostRepository;
import com.github.selftown.domain.post.response.PostResponse;
import com.github.selftown.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    @PostMapping
    public ResponseEntity<PostResponse> createPost(Authentication authentication,
                                                   @RequestParam("category") String category,
                                                   @RequestParam("title") String title,
                                                   @RequestParam("content") String content,
                                                   @RequestParam("files") List<MultipartFile> files) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = Long.parseLong(userDetails.getUsername());

        PostResponse response = postService.createPost(userId, category, title, content, files);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> getPosts() {
        List<PostResponse> responses = postService.getPosts();
        return ResponseEntity.ok(responses);
    }
}
