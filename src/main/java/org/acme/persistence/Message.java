package org.acme.persistence;

import io.quarkus.mongodb.panache.PanacheMongoEntity;

import java.util.ArrayList;
import java.util.List;

public class Message extends PanacheMongoEntity {

    public long postId;

    public String name;
    public String message;

    public int positiveReviews;
    public int negativeReviews;

    public List<Reply> replies = new ArrayList<>();

    public Message() {}

    public Message(long id, String name, String message) {
        this.postId = id;
        this.name = name;
        this.message = message;
    }

    public void appendReply(Reply message) {
        this.replies.add(message);
        update();
    }

    public static void incrementPositive(long postId) {
        update("{'$inc': {'positiveReviews': 1}}")
                .where("postId", postId);
    }

    public static void incrementNegative(long postId) {
        update("{'$inc': {'negativeReviews': 1}}")
                .where("postId", postId);
    }

    public static Message findByPostId(long postId) {
        return find("postId", postId).singleResult();
    }
}
