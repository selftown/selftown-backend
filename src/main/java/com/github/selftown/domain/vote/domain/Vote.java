package com.github.selftown.domain.vote.domain;

import com.github.selftown.domain.post.domain.Post;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

    @OneToMany(mappedBy = "vote", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<VoteOption> voteOptions = new ArrayList<>();


}
