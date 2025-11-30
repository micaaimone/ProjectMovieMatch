package com.example.demo.model.mappers.Chats;

import com.example.demo.model.DTOs.Chats.ChatMessageDTO;
import com.example.demo.model.entities.Chats.ChatMessageEntity;
import com.example.demo.model.entities.Chats.ChatRoomEntity;
import com.example.demo.model.entities.User.UsuarioEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ChatMessageMapper {

    private final ModelMapper modelMapper;


    public ChatMessageDTO toDTO(ChatMessageEntity entity) {
        ChatMessageDTO dto = modelMapper.map(entity, ChatMessageDTO.class);

        dto.setChatRoomId(entity.getChatRoom().getId());
        dto.setSenderId(entity.getSender().getId());

        return dto;
    }

    public ChatMessageEntity toEntity(ChatMessageDTO dto,
                                      ChatRoomEntity room,
                                      UsuarioEntity sender) {

        ChatMessageEntity entity = modelMapper.map(dto, ChatMessageEntity.class);
        entity.setChatRoom(room);
        entity.setSender(sender);

        return entity;
    }
}
