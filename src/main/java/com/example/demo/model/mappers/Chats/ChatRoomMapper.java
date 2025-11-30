package com.example.demo.model.mappers.Chats;

import com.example.demo.model.DTOs.Chats.ChatMessageDTO;
import com.example.demo.model.DTOs.Chats.ChatRoomDTO;
import com.example.demo.model.DTOs.Chats.ChatRoomDetailDTO;
import com.example.demo.model.entities.Chats.ChatMessageEntity;
import com.example.demo.model.entities.Chats.ChatParticipantsEntity;
import com.example.demo.model.entities.Chats.ChatRoomEntity;
import com.example.demo.model.repositories.Chats.ChatMessageRepository;
import com.example.demo.model.repositories.Chats.ChatParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ChatRoomMapper {
    private final ModelMapper modelMapper;
    private final ChatMessageMapper chatMessageMapper;
    private final ChatParticipantRepository participantRepository;
    private final ChatMessageRepository messageRepository;



    public ChatRoomDTO toDTO(ChatRoomEntity entity) {

        ChatRoomDTO dto = modelMapper.map(entity, ChatRoomDTO.class);


        // participantes
        List<Long> participantIds = participantRepository
                .findByChatRoomId(entity.getId())
                .stream()
                .map(p -> p.getUser().getId())
                .toList();

        dto.setParticipantIds(participantIds);

        return dto;
    }

    public ChatRoomDetailDTO toDetailsDTO(ChatRoomEntity entity){
        ChatRoomDetailDTO detailsDTO = modelMapper.map(entity, ChatRoomDetailDTO.class);

        // participantes
        List<Long> participantIds = participantRepository
                .findByChatRoomId(entity.getId())
                .stream()
                .map(p -> p.getUser().getId())
                .toList();

        detailsDTO.setParticipantIds(participantIds);

        // mensajes del chat
        List<ChatMessageDTO> messageDTOs = messageRepository
                .findByChatRoomIdOrderBySentAtAsc(entity.getId())
                .stream()
                .map(chatMessageMapper::toDTO)
                .toList();

        detailsDTO.setMessages(messageDTOs);


        return detailsDTO;
    }

    public ChatRoomEntity toEntity(ChatRoomDTO dto) {
        return modelMapper.map(dto, ChatRoomEntity.class);
    }

}
