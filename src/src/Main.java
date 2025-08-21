import common.LamportClock;
import net.MulticastListener;
import net.TcpServer;
import rmi.RemoteNóImpl;

import java.rmi.registry.LocateRegistry;

public class Main {
    public static void main(String[] args) throws Exception {
        // args: nodeId grupo tipo porta ...
        String nodeId = args.length>0?args[0]:"A1";
        String grupo = args.length>1?args[1]:"A";
        int tcpPort = args.length>2?Integer.parseInt(args[2]):5000;

        LamportClock relogio = new LamportClock();

        // início TCP servidor
        new Thread(new TcpServer(tcpPort)).start();

        // início multicast listener
        new Thread(new MulticastListener("230.0.0.0", 4446)).start();

        // simple RMI for grupo B
        if (grupo.equals("B")) {
            try {
                LocateRegistry.createRegistry(1099);
            } catch (Exception ignored) {}
            RemoteNóImpl impl = new RemoteNóImpl(nodeId, relogio);
            java.rmi.Naming.rebind("//localhost/" + nodeId, impl);
            System.out.println("RMI no bound: " + nodeId);
        }

        System.out.println("Nó " + nodeId + " started. grupo=" + grupo);
    }
}