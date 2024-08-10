package com.github.selftown.domain.chat.domain;

import com.github.selftown.domain.message.domain.Message;
import com.github.selftown.domain.user.domain.User;
import com.github.selftown.global.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class ChatRoom extends BaseEntity {

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy = "chat", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Message> messages = new ArrayList<>();
}
