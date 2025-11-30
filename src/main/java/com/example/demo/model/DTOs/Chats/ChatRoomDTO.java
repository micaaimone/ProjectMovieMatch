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
public class ChatRoomDTO {
    private Long id;
    private String name;
    private Boolean isGroup;

    // pensaba hacer tipo wpp -> ultimo msj y hr
    private LocalDateTime lastMessageAt;
    private String lastMessagePreview;

    private List<Long> participantIds;
}
