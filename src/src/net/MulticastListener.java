package net;

import java.net.*;

public class MulticastListener implements Runnable {
    private final InetAddress group;
    private final int port;
    private volatile boolean running = true;

    public MulticastListener(String groupAddr, int port) throws Exception {
        this.group = InetAddress.getByName(groupAddr); this.port = port;
    }

    public void stop() { running = false; }

    @Override
    public void run() {
        try (MulticastSocket ms = new MulticastSocket(port)) {
            ms.joinGroup(group);
            byte[] buf = new byte[1024];
            while (running) {
                DatagramPacket p = new DatagramPacket(buf, buf.length);
                ms.receive(p);
                String msg = new String(p.getData(), 0, p.getLength());
                System.out.println("Multicast received: " + msg);
            }
            ms.leaveGroup(group);
        } catch (Exception e) {
            System.err.println("MulticastListener error: " + e.getMessage());
        }
    }
}