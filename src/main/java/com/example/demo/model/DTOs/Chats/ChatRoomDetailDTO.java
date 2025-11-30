package com.example.demo.model.DTOs.Chats;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder

public class ChatRoomDetailDTO {
    private Long id;
    private String name;
    private Boolean isGroup;
    private Long createdByUserId;
    private LocalDateTime createdAt;

    private List<Long> participantIds;

    // mensajes completos
    private List<ChatMessageDTO> messages;
}
