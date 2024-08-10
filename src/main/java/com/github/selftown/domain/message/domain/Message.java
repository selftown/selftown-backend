package com.github.selftown.domain.message.domain;

import com.github.selftown.domain.chat.domain.ChatRoom;
import com.github.selftown.global.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
public class Message extends BaseEntity {
    @ManyToOne
    @JoinColumn(name="chat_id")
    private ChatRoom chat;
}
