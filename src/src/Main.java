import common.LamportClock;
import net.MulticastListener;
import net.TcpServer;
import rmi.RemoteNodeImpl;

import java.rmi.registry.LocateRegistry;

public class Main {
    public static void main(String[] args) throws Exception {
        // args: nodeId group type port ...
        String nodeId = args.length>0?args[0]:"A1";
        String group = args.length>1?args[1]:"A";
        int tcpPort = args.length>2?Integer.parseInt(args[2]):5000;

        LamportClock clock = new LamportClock();

        // start TCP server
        new Thread(new TcpServer(tcpPort)).start();

        // start multicast listener
        new Thread(new MulticastListener("230.0.0.0", 4446)).start();

        // simple RMI for group B
        if (group.equals("B")) {
            try {
                LocateRegistry.createRegistry(1099);
            } catch (Exception ignored) {}
            RemoteNodeImpl impl = new RemoteNodeImpl(nodeId, clock);
            java.rmi.Naming.rebind("//localhost/" + nodeId, impl);
            System.out.println("RMI node bound: " + nodeId);
        }

        System.out.println("Node " + nodeId + " started. group=" + group);
    }
}