package net;

import core.SnapshotManager;

import java.net.*;
import java.nio.charset.StandardCharsets;

public class MulticastListener implements Runnable {
    private final InetAddress group;
    private final int port;
    private final SnapshotManager snapshot;
    private volatile boolean running = true;

    public MulticastListener(String groupAddr, int port, SnapshotManager snapshot) throws Exception {
        this.group = InetAddress.getByName(groupAddr); this.port = port; this.snapshot = snapshot;
    }

    public void stop() { running = false; }

    @Override
    public void run() {
        try (MulticastSocket socket = new MulticastSocket(port)) {
            socket.joinGroup(group);
            System.out.println("Multicast listening on " + group + ":" + port);
            byte[] buf = new byte[512];
            while (running) {
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
                if (msg.startsWith("MARKER|SNAP|")) {
                    String snapId = msg.substring("MARKER|SNAP|".length());
                    snapshot.receiveMarker(snapId, packet.getAddress().toString());
                }
            }
        } catch (Exception e) {
            if (running) System.err.println("MulticastListener error: " + e.getMessage());
        }
    }
}