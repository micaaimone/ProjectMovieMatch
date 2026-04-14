package com.example.demo.model.controllers.Chat;

import com.example.demo.model.DTOs.Chats.ChatMessageDTO;
import com.example.demo.model.services.Chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat")

public class ChatRestController {
    private final ChatService chatService;

    @Autowired
    public ChatRestController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/{chatRoomId}/historial")
    public ResponseEntity<List<ChatMessageDTO>> getHistorial(@PathVariable Long chatRoomId) {
        return ResponseEntity.ok(chatService.getHistorial(chatRoomId));
    }
}
