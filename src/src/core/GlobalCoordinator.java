package core;

import java.net.*;
import java.nio.charset.StandardCharsets;

public class GlobalCoordinator implements Runnable {
    private final String leaderId;
    private final String group; // "A" or "B"
    private final String mcastAddr = "230.0.0.0";
    private final int mcastPort = 4446;

    public GlobalCoordinator(String leaderId, String group) {
        this.leaderId = leaderId; this.group = group;
    }

    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket()) {
            String announce = "LEADER|" + group + "|" + leaderId;
            byte[] buf = announce.getBytes(StandardCharsets.UTF_8);
            DatagramPacket packet = new DatagramPacket(buf, buf.length,
                    InetAddress.getByName(mcastAddr), mcastPort);
            socket.send(packet);
            System.out.println("Announced leader " + leaderId + " for group " + group);

            // Listen briefly for the peer leader to decide supercoordinator
            long end = System.currentTimeMillis() + 5000;
            String otherLeader = null; String otherGroup = null;
            try (MulticastSocket ms = new MulticastSocket(mcastPort)) {
                ms.joinGroup(InetAddress.getByName(mcastAddr));
                ms.setSoTimeout(1000);
                while (System.currentTimeMillis() < end) {
                    try {
                        byte[] rbuf = new byte[256];
                        DatagramPacket dp = new DatagramPacket(rbuf, rbuf.length);
                        ms.receive(dp);
                        String msg = new String(dp.getData(), 0, dp.getLength(), StandardCharsets.UTF_8);
                        if (msg.startsWith("LEADER|")) {
                            String[] parts = msg.split("|");
                            String g = parts[1];
                            String id = parts[2];
                            if (!g.equals(group)) { otherLeader = id; otherGroup = g; break; }
                        }
                    } catch (SocketTimeoutException ignored) {}
                }
            }
            String superId = leaderId;
            if (otherLeader != null) {
                // simple tie-break: lexicographically larger ID wins
                superId = (leaderId.compareTo(otherLeader) >= 0) ? leaderId : otherLeader;
            }
            if (leaderId.equals(superId)) {
                // I am supercoordinator: trigger global snapshot
                String marker = "MARKER|SNAP|" + System.currentTimeMillis();
                byte[] m = marker.getBytes(StandardCharsets.UTF_8);
                DatagramPacket mp = new DatagramPacket(m, m.length,
                        InetAddress.getByName(mcastAddr), mcastPort);
                socket.send(mp);
                System.out.println("Supercoordinator " + leaderId + " initiated global snapshot.");
            } else {
                System.out.println("Leader " + leaderId + " recognized " + superId + " as supercoordinator.");
            }
        } catch (Exception e) {
            System.err.println("GlobalCoordinator error: " + e.getMessage());
        }
    }
}