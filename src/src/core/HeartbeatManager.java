package core;

import java.util.*;
import java.util.concurrent.*;

public class HeartbeatManager {
    private final Map<String,Integer> misses = new ConcurrentHashMap<>();
    private final ScheduledExecutorService sched = Executors.newScheduledThreadPool(1);
    private final int threshold = 3;
    private final List<String> peers;

    public HeartbeatManager(List<String> peers) {
        this.peers = peers;
    }

    public void start() {
        sched.scheduleAtFixedRate(this::sendAndCheck, 0, 1, TimeUnit.SECONDS);
    }

    private void sendAndCheck() {
        for (String peer : peers) {
            boolean ok = pingPeer(peer);
            if (!ok) {
                misses.put(peer, misses.getOrDefault(peer,0)+1);
                if (misses.get(peer) >= threshold) {
                    System.out.println("Peer " + peer + " considered inactive -> substituting (simulated).");
                    misses.remove(peer);
                    substitutePeer(peer);
                }
            } else {
                misses.remove(peer);
            }
        }
    }

    private boolean pingPeer(String peer) {
        // Simplificação: aqui se chamaria RMI/gRPC/TCP heartbeat. Retornamos true por padrão.
        return true;
    }

    private void substitutePeer(String peer) {
        System.out.println("Simulating replacement for " + peer);
        // Implementar recriação de nó simulada (ex.: instanciar NodeReplacement)
    }

    public void stop() { sched.shutdownNow(); }
}