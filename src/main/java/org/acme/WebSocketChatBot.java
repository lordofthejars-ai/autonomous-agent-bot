package org.acme;

import io.quarkus.runtime.Startup;
import io.quarkus.websockets.next.OnTextMessage;
import io.quarkus.websockets.next.WebSocket;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.persistence.Product;

@ApplicationScoped
@WebSocket(path = "/websocket")
public class WebSocketChatBot {

    @Startup
    public void populateProducts() {
        Product product = new Product();

        product.name = "CoolTV";
        product.monitor = "43\"";
        product.price = 359;
        product.resolution = "4k";
        product.weight = 15;
        product.description = """
                Perfect TV for everyone who wants to watch a good movie.
                The image quality is superb, and it comes with great audio dolby surround.
                
                Moreover, a lot of applications are installed like YouTube, Amazon Prime, Disney+, and Netflix.
                
                The TV also integrates with Alexa voice system so it can be controlled either with the remote controller or Alexa.
                
                The TV contains 3 HDMI input ports, making it perfect for people who plays with multiple consoles.
                """;

        product.persist();
    }

    public record ChatMessage (String type, long id, long replyTo, String name, String message) {}

    @Inject
    MessageOperations messageOperations;

    @OnTextMessage(broadcast = true)
    public ChatMessage onMessage(ChatMessage message) {
        return switch (message.type()) {
            case "post" -> messageOperations.newPost(message);
            case "reply" -> messageOperations.reply(message);
            default -> null;
        };
    }

}
