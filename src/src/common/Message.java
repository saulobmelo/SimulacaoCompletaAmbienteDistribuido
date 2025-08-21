package common;

import java.io.Serializable;
import java.util.UUID;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    public final String origem;
    public final String destino;
    public final String conteudo;
    public final int lamport;
    public final UUID id;

    public Message(String origem, String destino, String conteudo, int lamport) {
        this.origem = origem;
        this.destino = destino;
        this.conteudo = conteudo;
        this.lamport = lamport;
        this.id = UUID.randomUUID();
    }

    @Override
    public String toString() {
        return String.format("Message[%s -> %s] id=%s lamport=%d conteudo=%s", origem, destino, id, lamport, conteudo);
    }
}