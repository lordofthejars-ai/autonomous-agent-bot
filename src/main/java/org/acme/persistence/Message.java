package org.acme.persistence;

import java.util.ArrayList;
import java.util.List;

public class Message {

    public long postId;

    public String name;
    public String message;

    public List<Message> replies = new ArrayList<>();

    public Message(long id, String name, String message) {
        this.postId = id;
        this.name = name;
        this.message = message;
    }

    public void appendReply(Message message) {
        this.replies.add(message);
    }
}
