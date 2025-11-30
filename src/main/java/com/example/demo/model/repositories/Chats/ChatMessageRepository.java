package com.example.demo.model.repositories.Chats;

import com.example.demo.model.entities.Chats.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

    List<ChatMessageEntity> findByChatRoomIdOrderBySentAtAsc(Long chatRoomId);
}