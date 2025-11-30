package com.example.demo.model.mappers.Chats;

import com.example.demo.model.DTOs.Chats.ChatParticipantDTO;
import com.example.demo.model.entities.Chats.ChatParticipantsEntity;
import com.example.demo.model.entities.Chats.ChatRoomEntity;
import com.example.demo.model.entities.User.UsuarioEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ChatParticipantMapper {

    private final ModelMapper modelMapper;

    public ChatParticipantDTO toDTO(ChatParticipantsEntity entity){
        ChatParticipantDTO dto = modelMapper.map(entity, ChatParticipantDTO.class);

        dto.setChatRoomId(entity.getChatRoom().getId());
        dto.setUserId(entity.getUser().getId());

        return dto;
    }

    public ChatParticipantsEntity toEntity(ChatParticipantDTO dto,
                                           ChatRoomEntity room,
                                           UsuarioEntity user) {

        ChatParticipantsEntity entity = modelMapper.map(dto, ChatParticipantsEntity.class);
        entity.setChatRoom(room);
        entity.setUser(user);

        return entity;
    }
}
