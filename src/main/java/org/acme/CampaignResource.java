package org.acme;

import io.quarkus.websockets.next.OpenConnections;
import io.quarkus.websockets.next.WebSocketConnection;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import java.util.Collection;
import org.acme.ai.MarketingCampaign;
import org.jboss.logging.Logger;

@Path("/")
public class CampaignResource {

    record Message(String message) {}

    record Response(String response) {}

    @Inject
    MarketingCampaign marketingCampaign;

    @Inject
    OpenConnections connections;

    @Inject
    MessageOperations messageOperations;

    @Inject
    Logger logger;

    @POST
    @Path("/createCampaign")
    public Response createCampaign(Message message) {

        String campaign = message.message();

        logger.infof("Create a Campaign with the following description:  %s", campaign);

        // Generates the message for the tweet/campaign
        String socialMediaMessage = marketingCampaign.generate(campaign);

        // Stores the message into MongoDB to query or manage
        ChatMessage chatMessage = new ChatMessage("post", System.currentTimeMillis(), 0, "AutoBot", socialMediaMessage);
        this.messageOperations.newPost(chatMessage);

        // Sends to connections to "our tweeter"/announcements portal to show the new campaign
        final Collection<WebSocketConnection> announcements = connections.findByEndpointId("announcements");
        System.out.println(announcements.isEmpty());
        announcements.forEach(c -> c.sendText(chatMessage));

        return new Response("Campaign with %s id created.".formatted(chatMessage.id()));
    }
}