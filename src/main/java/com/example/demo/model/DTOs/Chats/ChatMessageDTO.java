package com.example.demo.model.DTOs.Chats;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder

public class ChatMessageDTO {

    private Long id;
    private Long chatRoomId;
    private Long senderId;
    private String content;
    private String type;
    private LocalDateTime sentAt;

}
