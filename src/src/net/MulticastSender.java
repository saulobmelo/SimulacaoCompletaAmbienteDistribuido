package net;

import java.net.*;

public class MulticastSender {
    private final InetAddress group;
    private final int port;
    public MulticastSender(String groupAddr, int port) throws Exception {
        this.group = InetAddress.getByName(groupAddr); this.port = port;
    }

    public void send(String msg) throws Exception {
        try (DatagramSocket ds = new DatagramSocket()) {
            byte[] data = msg.getBytes();
            DatagramPacket p = new DatagramPacket(data, data.length, group, port);
            ds.send(p);
        }
    }
}