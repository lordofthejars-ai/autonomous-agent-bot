package org.acme;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.quarkus.websockets.next.BasicWebSocketConnector;
import io.quarkus.websockets.next.WebSocketClientConnection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.ai.SentimentAnalyzer;
import org.acme.persistence.Message;
import org.acme.persistence.Reply;

@ApplicationScoped
public class MessageOperations {

    private final MeterRegistry registry;

    public MessageOperations(MeterRegistry meterRegistry) {
        this.registry = meterRegistry;
    }

    @Inject
    SentimentAnalyzer sentimentAnalyzer;

    public WebSocketChatBot.ChatMessage reply(WebSocketChatBot.ChatMessage chatMessage) {

        final Message m = Message.findByPostId(chatMessage.replyTo());
        String message = updateMessage(chatMessage, m);

        updateSentiment(message, m);

        if (m.negativeReviews >= 3) {
            // It is time for a new tweet fixing the errors
        }

        return chatMessage;
    }

    @Inject
    BasicWebSocketConnector connection;

    private void updateSentiment(String message, Message m) {
        switch (sentimentAnalyzer.analyze(message)) {
            case POSITIVE -> {
                m.positiveReviews++;
                m.update();
            }
            case NEGATIVE -> {
                m.negativeReviews++;
                m.update();
            }
        }
    }

    private static String updateMessage(WebSocketChatBot.ChatMessage chatMessage, Message m) {
        String message = chatMessage.message();
        Reply reply = new Reply(chatMessage.name(), message);

        m.appendReply(reply);
        return message;
    }

    public WebSocketChatBot.ChatMessage newPost(WebSocketChatBot.ChatMessage chatMessage) {

        final Message message = new Message(chatMessage.id(),
                chatMessage.name(),
                chatMessage.message());

        //Gauge

        message.persist();

        return chatMessage;
    }

}
