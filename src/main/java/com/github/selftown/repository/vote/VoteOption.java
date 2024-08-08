package com.github.selftown.repository.vote;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class VoteOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String option;

    @ManyToOne
    @JoinColumn(name ="vote_id")
    private Vote vote;
}
