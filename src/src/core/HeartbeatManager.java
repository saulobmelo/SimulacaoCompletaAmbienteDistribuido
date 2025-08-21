package core;

import java.util.*;
import java.util.concurrent.*;

public class HeartbeatManager {
    private final Map<String,Integer> falhas = new ConcurrentHashMap<>();
    private final ScheduledExecutorService sched = Executors.newScheduledThreadPool(1);
    private final int threshold = 3;
    private final List<String> pares;

    public HeartbeatManager(List<String> pares) {
        this.pares = pares;
    }

    public void start() {
        sched.scheduleAtFixedRate(this::sendAndCheck, 0, 1, TimeUnit.SECONDS);
    }

    private void sendAndCheck() {
        for (String peer : pares) {
            boolean ok = pingPar(peer);
            if (!ok) {
                falhas.put(peer, falhas.getOrDefault(peer,0)+1);
                if (falhas.get(peer) >= threshold) {
                    System.out.println("Par " + peer + " considerado inativo -> substituindo (simulado).");
                    falhas.remove(peer);
                    substitutePar(peer);
                }
            } else {
                falhas.remove(peer);
            }
        }
    }

    private boolean pingPar(String peer) {
        // Simplificação: aqui se chamaria RMI/gRPC/TCP heartbeat. Retornamos true por padrão.
        return true;
    }

    private void substitutePar(String peer) {
        System.out.println("Simulando substituição para " + peer);
        // Implementar recriação de nó simulada (ex.: instanciar NóReplacement)
    }

    public void stop() { sched.shutdownNow(); }
}