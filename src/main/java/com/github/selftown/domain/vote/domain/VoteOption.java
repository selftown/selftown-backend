package com.github.selftown.domain.vote.domain;

import com.github.selftown.domain.vote.domain.Vote;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class VoteOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name="option_text", nullable = false)
    private String optionText;

    @ManyToOne
    @JoinColumn(name ="vote_id")
    private Vote vote;
}
