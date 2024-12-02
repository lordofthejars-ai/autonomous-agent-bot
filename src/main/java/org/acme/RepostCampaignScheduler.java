package org.acme;

import io.quarkus.scheduler.Scheduled;
import io.quarkus.websockets.next.OpenConnections;
import io.quarkus.websockets.next.WebSocketConnection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Collection;
import org.acme.ai.MarketingCampaign;
import org.acme.persistence.Message;
import org.jboss.logging.Logger;

@ApplicationScoped
public class RepostCampaignScheduler {

    @Inject
    MarketingCampaign marketingCampaign;

    @Inject
    OpenConnections connections;

    @Inject
    Logger logger;

    @Scheduled(every = "1m", delayed = "1m")
    void repost() {

        logger.infof("Reposting schedule");

        Message.findMessagesWithPositiveComments()
            .stream().filter(m -> m.negativeReviews < 2)
            .forEach(c -> {
                String socialMediaMessage = marketingCampaign.rebump(c.message, c.replies);

                // Stores the message into MongoDB to query or manage
                ChatMessage chatMessage = new ChatMessage("post", System.currentTimeMillis(), 0, "AutoBot", socialMediaMessage);

                final Message message = new Message(chatMessage.id(),
                    chatMessage.name(),
                    chatMessage.message());

                message.persist();

                // Sends to connections to "our tweeter"/announcements portal to show the new campaign
                final Collection<WebSocketConnection> announcements = connections.findByEndpointId("announcements");
                announcements.forEach(socket -> socket.sendTextAndAwait(chatMessage));

            });
    }
}
