package com.example.demo.model.repositories.Chats;

import com.example.demo.model.entities.Chats.ChatParticipantsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatParticipantRepository extends JpaRepository<ChatParticipantsEntity, Long> {

    List<ChatParticipantsEntity> findByChatRoomId(Long chatRoomId);

    List<ChatParticipantsEntity> findByUserId(Long userId);

    boolean existsByChatRoomIdAndUserId(Long chatRoomId, Long userId);
}