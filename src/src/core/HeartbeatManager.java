package core;

import java.util.*;
import java.util.concurrent.*;
import java.rmi.Naming;
import rmi.RemoteNode;

public class HeartbeatManager {
    private final Map<String,Integer> misses = new ConcurrentHashMap<>();
    private final ScheduledExecutorService sched = Executors.newScheduledThreadPool(1);
    private final int threshold = 3;
    private final List<String> peers;
    private final String group; // "A" (gRPC) or "B" (RMI)

    public HeartbeatManager(String group, List<String> peers) {
        this.group = group;
        this.peers = peers;
    }

    public void start() {
        sched.scheduleAtFixedRate(() -> {
            for (String p : peers) {
                boolean ok = pingPeer(p);
                if (!ok) {
                    int c = misses.merge(p, 1, Integer::sum);
                    System.err.println("Heartbeat miss from " + p + " (" + c + ")");
                    if (c >= threshold) {
                        substitutePeer(p);
                        misses.put(p, 0);
                    }
                } else {
                    misses.put(p, 0);
                }
            }
        }, 0, 2, java.util.concurrent.TimeUnit.SECONDS);
    }

    private boolean pingPeer(String peer) {
        try {
            if ("B".equalsIgnoreCase(group)) {
                RemoteNode stub = (RemoteNode) Naming.lookup("//localhost/" + peer);
                return stub.heartbeat();
            } else {
                // gRPC placeholder: call GrpcNodeClient.heartbeat(peer)
                // return GrpcNodeClient.heartbeat(peer);
                return true; // until gRPC client is wired
            }
        } catch (Exception e) {
            return false;
        }
    }

    private void substitutePeer(String peer) {
        System.out.println("Simulating replacement for " + peer);
        // Implementar substituição: ex. registrar novo nó/ID e anunciar via multicast
    }

    public void stop() { sched.shutdownNow(); }
}