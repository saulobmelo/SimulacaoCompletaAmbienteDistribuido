import common.LamportClock;
import common.Message;
import net.MulticastListener;
import net.TcpServer;
import net.TcpClient;
import rmi.RemoteNóImpl;

import java.rmi.registry.LocateRegistry;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        // args: nodeId grupo porta listaPares
        String nodeId = args.length > 0 ? args[0] : "A1";
        String grupo = args.length > 1 ? args[1] : "A";
        int tcpPort = args.length > 2 ? Integer.parseInt(args[2]) : 5000;
        String peers = args.length > 3 ? args[3] : "";

        LamportClock relogio = new LamportClock();

        // Inicia o servidor TCP
        new Thread(new TcpServer(tcpPort)).start();

        // Inicia o listener multicast
        new Thread(new MulticastListener("230.0.0.0", 4446)).start();

        // Se for grupo B, sobe também RMI
        if (grupo.equals("B")) {
            try {
                LocateRegistry.createRegistry(1099);
            } catch (Exception ignored) {
            }
            RemoteNóImpl impl = new RemoteNóImpl(nodeId, relogio);
            java.rmi.Naming.rebind("//localhost/" + nodeId, impl);
            System.out.println("Nó RMI registrado: " + nodeId);
        }

        System.out.println("Nó " + nodeId + " iniciado. grupo=" + grupo);

        // espera 2 segundos para garantir que os servidores já subiram
        Thread.sleep(2000);

        // Envia mensagens de teste automáticas para os pares
        for (int i=0; i<3; i++) {
            if (!peers.isEmpty()) {
                Arrays.stream(peers.split(",")).forEach(p -> {
                    try {
                        String[] parts = p.split(":");
                        String peerId = parts[0];
                        int peerPort = Integer.parseInt(parts[1]);

                        // incrementa relógio Lamport antes de enviar
                        int ts = relogio.tick();

                        Message msg = new Message(nodeId, peerId,
                                "Mensagem de teste automática de " + nodeId + " para " + peerId,
                                ts);

                        boolean ok = TcpClient.send("localhost", peerPort, msg);
                        if (ok) {
                            System.out.println("Mensagem enviada automaticamente de "
                                    + nodeId + " para " + peerId + " (lamport=" + ts + ")");
                        }
                    } catch (Exception e) {
                        System.err.println("Falha ao enviar mensagem de teste: " + e.getMessage());
                    }
                });
            }
        }
    }
}