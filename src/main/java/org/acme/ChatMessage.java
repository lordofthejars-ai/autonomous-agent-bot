package org.acme;

public record ChatMessage(String type, long id, long replyTo, String name, String message) {
}
