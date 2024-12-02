package org.acme;


import io.quarkus.virtual.threads.VirtualThreads;
import io.quarkus.websockets.next.BasicWebSocketConnector;
import io.quarkus.websockets.next.OpenConnections;
import io.quarkus.websockets.next.WebSocketClientConnection;
import io.quarkus.websockets.next.WebSocketConnection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import org.acme.ai.MarketingCampaign;
import org.acme.ai.SentimentAnalyzer;
import org.acme.persistence.Message;
import org.acme.persistence.Reply;
import org.jboss.logging.Logger;

@ApplicationScoped
public class MessageOperations {

    @Inject
    SentimentAnalyzer sentimentAnalyzer;

    @Inject
    Logger logger;

    public ChatMessage reply(ChatMessage chatMessage) {

        final Message m = Message.findByPostId(chatMessage.replyTo());
        String message = updateMessage(chatMessage, m);

        updateSentiment(message, m);

        if (m.negativeReviews == 3) {
            // It is time for a new tweet fixing the errors
            // The process takes some time so we execute in another thread, and it can be executed async as the result is not expected here
            rewriteAnnouncement(m);
        }

        return chatMessage;
    }


    @Inject
    MarketingCampaign marketingCampaign;

    @Inject
    OpenConnections connections;

    private void rewriteAnnouncement(Message originalMessage) {

        logger.infof("Rewrite a Campaign");

        // Generates the message for the tweet/campaign
        String socialMediaMessage = marketingCampaign.recreate(originalMessage.message, originalMessage.replies);

        // Stores the message into MongoDB to query or manage
        ChatMessage chatMessage = new ChatMessage("post", System.currentTimeMillis(), 0, "AutoBot", socialMediaMessage);
        this.newPost(chatMessage);

        // Sends to connections to "our tweeter"/announcements portal to show the new campaign
        final Collection<WebSocketConnection> announcements = connections.findByEndpointId("announcements");

        announcements.forEach(c -> c.sendTextAndAwait(chatMessage));

    }

    private void updateSentiment(String replyMessage, Message m) {
        switch (sentimentAnalyzer.analyze(replyMessage)) {
            case POSITIVE -> {
                logger.infof("Categorized as Positive Message: %s", replyMessage);
                m.positiveReviews++;
                m.update();
            }
            case NEGATIVE -> {
                logger.infof("Categorized as Negative Message: %s", replyMessage);
                m.negativeReviews++;
                m.update();
            }
        }
    }

    private static String updateMessage(ChatMessage chatMessage, Message m) {
        String message = chatMessage.message();
        Reply reply = new Reply(chatMessage.name(), message);

        m.appendReply(reply);
        return message;
    }

    public ChatMessage newPost(ChatMessage chatMessage) {

        final Message message = new Message(chatMessage.id(),
                chatMessage.name(),
                chatMessage.message());

        message.persist();

        return chatMessage;
    }

}
