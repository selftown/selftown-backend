package com.github.selftown.domain.post.service;

import com.github.selftown.domain.post.entity.Category;
import com.github.selftown.domain.post.entity.Post;
import com.github.selftown.domain.post.entity.PostImage;
import com.github.selftown.domain.post.repository.PostRepository;
import com.github.selftown.domain.post.response.PostResponse;
import com.github.selftown.domain.user.entity.User;
import com.github.selftown.domain.user.repository.UserRepository;
import com.github.selftown.global.exception.CategoryException;
import com.github.selftown.global.exception.UserException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PostService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;
    private Path resolvedUploadDir;  // 실제 파일 시스템 경로
    @PostConstruct
    public void init() {
        // 애플리케이션이 실행되는 실제 경로로 설정
        this.resolvedUploadDir = Paths.get(new File(uploadDir).getAbsolutePath());

        // 디렉토리가 없으면 생성
        try {
            Files.createDirectories(resolvedUploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create upload directory " + resolvedUploadDir, e);
        }
    }

    public List<PostResponse> getPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(post -> PostResponse.builder()
                        .postId(post.getId())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .category(String.valueOf(post.getCategory()))
                        .userName(post.getUser().getName())
                        .userProfileImage(post.getUser().getProfileImage())
                        .userId(post.getUser().getId())
                        .created_at(post.getCreatedAt())
                        .imageUrls(post.getPostImages().stream()
                                .map(PostImage::getImageUrl)
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }
    public PostResponse createPost(Long userId, String category, String title, String content, List<MultipartFile> files) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserException.UserNotFountException::new);

        Category categoryEnum = Stream.of(Category.values())
                .filter(c -> c.name().equals(category))
                .findFirst()
                .orElseThrow(CategoryException.CategoryNotFoundException::new);

        List<PostImage> postImages = files.stream()
                .map(this::saveFileToLocalAndCreatePostImage)
                .collect(Collectors.toList());

        Post post = Post.builder()
                .title(title)
                .content(content)
                .category(categoryEnum)
                .user(user)
                .postImages(postImages)
                .build();

        postImages.forEach(image -> image.setPost(post));
        Post savedPost = postRepository.save(post);

        return PostResponse.builder()
                .postId(savedPost.getId())
                .title(savedPost.getTitle())
                .content(savedPost.getContent())
                .category(String.valueOf(savedPost.getCategory()))
                .userName(savedPost.getUser().getName())
                .userProfileImage(savedPost.getUser().getProfileImage())
                .userId(savedPost.getUser().getId())
                .imageUrls(savedPost.getPostImages().stream()
                        .map(PostImage::getImageUrl)
                        .collect(Collectors.toList()))
                .build();
    }

    public PostImage saveFileToLocalAndCreatePostImage(MultipartFile file) {
        try {
            String fileExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
            String newFileName = UUID.randomUUID().toString() + "." + fileExtension;

            // 실제 경로를 사용하여 파일 저장
            Path filePath = resolvedUploadDir.resolve(newFileName);
            file.transferTo(filePath.toFile());

            return PostImage.builder()
                    .imageUrl("/upload/" + newFileName)  // 웹 URL 경로로 설정
                    .build();
        } catch(Exception e) {
            throw new RuntimeException("Failed to save file " + e.getMessage());
        }
    }

}
