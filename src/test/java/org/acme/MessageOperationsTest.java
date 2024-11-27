package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@QuarkusTest
public class MessageOperationsTest {

    @Inject
    MessageOperations messageOperations;

    @Test
    public void shouldCreateAMarketingMessage() {

        ChatMessage message = new ChatMessage(
                "post", 1, 0, "Alex",
                "create a message for CoolTV product, focusing on dimension, resolution and short description features.");

        System.out.println(messageOperations.newPost(message));

    }

}
