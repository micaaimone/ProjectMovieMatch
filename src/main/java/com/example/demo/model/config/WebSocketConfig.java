package com.example.demo.model.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
//configuramos un broker para la comunicacion con los clientes
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    //nos permite habilitar un broker (es un componente que enruta mensajes)
    //El broker relaciona a los clientes entre sí a través del servidor.
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        //enableSimpleBroker define las rutas que maneja el broker interno
        // para enviar mensajes a los clientes (Son rutas de salida del servidor)
        //Los clientes se suscriben a estas rutas.
        //siendo topic el de chat grupales y queue para chats privados
        config.enableSimpleBroker("/topic", "/queue");
        //setApplicationDestinationPrefixes sirve para determinar la ruta desde el front.
        // osea -> todo mensaje que venga del front hacia el backend tiene q empezar con /app
        config.setApplicationDestinationPrefixes("/app");
    }

    //registramos los endpoints
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //es la "puerta" donde se abre la conexion WebSocket
        registry.addEndpoint("/chat-socket")
                .setAllowedOrigins("*") //permite que el front se conecte en cualquier puerto
                .withSockJS(); //es una librería que asegura compatibilidad para cuando un navegador NO soporta WebSocket puro
    }

    //| `/chat-socket`              ->  La puerta física donde entran al edificio |
    //| `/app/...`                  ->  El mostrador donde entregan pedidos       |
    //| `/topic/...` y `/queue/...` ->  Los buzones donde reciben mensajes        |

}
