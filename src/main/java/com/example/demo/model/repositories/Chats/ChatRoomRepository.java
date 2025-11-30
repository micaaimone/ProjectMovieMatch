package com.example.demo.model.repositories.Chats;

import com.example.demo.model.entities.Chats.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {
}
