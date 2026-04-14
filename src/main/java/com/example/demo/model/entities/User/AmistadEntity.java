package com.example.demo.model.entities.User;

import com.example.demo.model.entities.Chats.ChatRoomEntity;
import com.example.demo.model.enums.EstadoSolicitud;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity(name = "solicitudes")
public class AmistadEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_solicitud;

    private long idEmisor;

    private long idReceptor;

    private String username;

    private EstadoSolicitud estadoSolicitud;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "chat_room_id")
    private ChatRoomEntity chatRoom;

}
