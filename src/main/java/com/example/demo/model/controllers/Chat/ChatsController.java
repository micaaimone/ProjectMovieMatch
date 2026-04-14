package com.example.demo.model.controllers.Chat;

import com.example.demo.model.DTOs.Chats.ChatMessageDTO;
import com.example.demo.model.services.Chat.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatsController {
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;
    // SimpMessagingTemplate es el componente de Spring que te permite
    // mandar mensajes a destinos específicos desde el servidor


    public ChatsController(ChatService chatService, SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }

    //chat priv
    //ese payload vendria siendo el requestBody (toma el JSON y lo convierte en el objeto d chatdto)
    @MessageMapping("/chat.privado")
    public void privateMessage(@Payload ChatMessageDTO dto){
        ChatMessageDTO respuesta = chatService.sendMessage(
                dto.getChatRoomId(),
                dto.getSenderId(),
                dto.getContent()
        );

        // Ambos usuarios suscritos al topic del chat reciben el mensaje
        messagingTemplate.convertAndSend(
                "/topic/privado/" + dto.getChatRoomId(),
                respuesta
        );
    }

    // chat grupal
    @MessageMapping("/chat.grupo")
    public void mensajeGrupal(@Payload ChatMessageDTO dto) {

        ChatMessageDTO respuesta = chatService.sendMessage(
                dto.getChatRoomId(),
                dto.getSenderId(),
                dto.getContent()
        );

        // Todos los suscritos a este topic lo reciben
        messagingTemplate.convertAndSend(
                "/topic/grupo/" + dto.getChatRoomId(),
                respuesta
        );
    }
}
