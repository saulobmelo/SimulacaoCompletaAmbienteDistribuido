package net;

import java.net.*;

public class MulticastSender {
    private final InetAddress grupo;
    private final int porta;
    public MulticastSender(String groupAddr, int porta) throws Exception {
        this.grupo = InetAddress.getByName(groupAddr); this.porta = porta;
    }

    public void send(String msg) throws Exception {
        try (DatagramSocket ds = new DatagramSocket()) {
            byte[] data = msg.getBytes();
            DatagramPacket p = new DatagramPacket(data, data.length, grupo, porta);
            ds.send(p);
        }
    }
}