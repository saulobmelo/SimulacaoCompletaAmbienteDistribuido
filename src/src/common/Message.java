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

    public Message(String from, String to, String payload, int lamport) {
        this.from = from;
        this.to = to;
        this.payload = payload;
        this.lamport = lamport;
        this.id = UUID.randomUUID();
    }

    @Override
    public String toString() {
        return String.format("Message[%s -> %s] id=%s lamport=%d payload=%s", from, to, id, lamport, payload);
    }
}