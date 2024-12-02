package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import org.acme.ai.MarketingCampaign;
import org.acme.persistence.Reply;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class MarketingCampaignTest {

    @Inject
    MarketingCampaign marketingCampaign;

    @Test
    public void shouldCreateAMarketingMessage() {
        String message =
                marketingCampaign
                        .generate("create a message for CoolTV product, focusing on dimension, resolution and short description features. Not necessary to show the price.");

        assertThat(message)
                .contains("CoolTV")
                .containsIgnoringCase("4k");

    }

    @Test
    public void shouldRewriteBasedOnComments() {

        String originalMessage = """
            Transform your viewing experience with CoolTV! Enjoy a stunning 43" screen at 4K resolution. Perfect for movie lovers, it offers superb image quality and rich audio. Stream your favorites on apps like Netflix and control it with Alexa! #CoolTV
            """;

        List<Reply> replies= List.of(
            new Reply("A", "I think the post is useless as it is not showing the price"),
            new Reply("B", "Where is the price on the announcement? Without this, the message is not useful."),
            new Reply("C", "No price, no way. Impossible to take this message serious."));

        assertThat(marketingCampaign.recreate(originalMessage, replies)).contains("359");


    }

}
