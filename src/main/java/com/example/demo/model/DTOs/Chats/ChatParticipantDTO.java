package com.example.demo.model.DTOs.Chats;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder

public class ChatParticipantDTO {
    private Long id;
    private Long chatRoomId;
    private Long userId;
    private LocalDateTime joinedAt;
}
