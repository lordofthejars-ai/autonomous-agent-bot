package org.acme;

import io.quarkus.websockets.next.OnTextMessage;
import io.quarkus.websockets.next.WebSocket;

@WebSocket(path = "/websocket")
public class WebSocketChatBot {

/**    private final SessionScopedChatBot bot;

    public WebSocketChatBot(SessionScopedChatBot bot) {
        this.bot = bot;
    }**/

    public record ChatMessage (String type, long id, long replyTo, String name, String message) {}

    @OnTextMessage(broadcast = true)
    public ChatMessage onMessage(ChatMessage message) {
        System.out.println(message);
        return message;
    }

}
