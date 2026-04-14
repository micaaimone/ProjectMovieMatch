package com.example.demo.model.services.Chat;

import com.example.demo.model.DTOs.Chats.ChatMessageDTO;
import com.example.demo.model.entities.Chats.ChatMessageEntity;
import com.example.demo.model.entities.Chats.ChatRoomEntity;
import com.example.demo.model.entities.User.UsuarioEntity;
import com.example.demo.model.mappers.Chats.ChatMessageMapper;
import com.example.demo.model.repositories.Chats.ChatMessageRepository;
import com.example.demo.model.repositories.Chats.ChatRoomRepository;
import com.example.demo.model.repositories.Usuarios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UsuarioRepository usuarioRepository;
    private final ChatMessageMapper chatMessageMapper;

    @Autowired
    public ChatService(ChatRoomRepository chatRoomRepository, ChatMessageRepository chatMessageRepository, UsuarioRepository usuarioRepository, ChatMessageMapper chatMessageMapper) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.usuarioRepository = usuarioRepository;
        this.chatMessageMapper = chatMessageMapper;
    }

    public ChatMessageDTO sendMessage(Long chatRoomId, Long userId, String content){
        ChatRoomEntity chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("Chat no encontrado"));

        UsuarioEntity user = usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        ChatMessageEntity message = new ChatMessageEntity();
        message.setChatRoom(chatRoom);
        message.setSender(user);
        message.setContent(content);

        ChatMessageEntity save = chatMessageRepository.save(message);

        return chatMessageMapper.toDTO(save);
    }

    public List<ChatMessageDTO> getHistorial(Long chatRoomId){
        ChatRoomEntity chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RuntimeException("Chat no encontrado"));

        return chatMessageRepository.findByChatRoomIdOrderBySentAtAsc(chatRoomId)
                .stream()
                .map(chatMessageMapper::toDTO)
                .toList();
    }
}
