package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.acme.persistence.Message;
import org.acme.persistence.Reply;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class MessageTest {

    @Test
    public void shouldIncrementNegative() {
        Message m = new Message(1, "A", "B");
        m.persist();

        Message.incrementNegative(1);

        Message byPostId = Message.findByPostId(1);
        assertThat(byPostId.negativeReviews).isEqualTo(1);
    }

    @Test
    public void shouldAddAReply() {
        Message m = new Message(2, "A", "B");
        m.persist();

        m.appendReply(new Reply("B", "C"));
        Message byPostId = Message.findByPostId(2);

        assertThat(byPostId.replies).hasSize(1);


    }

}
