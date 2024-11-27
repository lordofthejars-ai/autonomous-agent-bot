package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.acme.ai.MarketingCampaignGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class MarketingCampaignGeneratorTest {

    @Inject
    MarketingCampaignGenerator marketingCampaignGenerator;

    @Test
    public void shouldCreateAMarketingMessage() {
        String message =
                marketingCampaignGenerator
                        .message("create a message for CoolTV product, focusing on dimension, resolution and short description features. Not necessary to show the price.");

        assertThat(message)
                .contains("CoolTV")
                .containsIgnoringCase("4k");

    }

}
