package common;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    public final String from;
    public final String to;
    public final String payload;
    public final int lamport;
    public final UUID id;
    public final String type;   // e.g. INFO, SENSITIVE, CONTROL
    public final String token;  // bearer token for access control

    // Backward-compatible: assume INFO and no token
    public Message(String from, String to, String payload, int lamport) {
        this(from, to, payload, lamport, "INFO", null);
    }

    public Message(String from, String to, String payload, int lamport, String type, String token) {
        this.from = from;
        this.to = to;
        this.payload = payload;
        this.lamport = lamport;
        this.id = java.util.UUID.randomUUID();
        this.type = type;
        this.token = token;
    }

    @Override
    public String toString() {
        return String.format("Message[%s -> %s] id=%s lamport=%d type=%s payload=%s",
                from, to, id, lamport, type, payload);
    }
}