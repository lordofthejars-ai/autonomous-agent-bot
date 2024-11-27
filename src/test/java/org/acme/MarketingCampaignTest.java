package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.acme.ai.MarketingCampaign;
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

}
