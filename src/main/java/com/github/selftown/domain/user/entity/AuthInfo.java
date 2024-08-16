package com.github.selftown.domain.user.entity;

import com.github.selftown.domain.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class AuthInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="auth_info_id")
    private Long id;

    @OneToOne(mappedBy = "authInfo")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @NotNull
    private String socialId;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Provider provider;
}
