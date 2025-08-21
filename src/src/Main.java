import common.LamportClock;
import core.BullyElection;
import core.RingElection;
import core.HeartbeatManager;
import core.GlobalCoordinator;
import core.SnapshotManager;
import net.MulticastListener;
import net.TcpServer;
import rmi.RemoteNodeImpl;

import java.rmi.registry.LocateRegistry;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        // args: nodeId group tcpPort peersCSV(hyphen-separated IDs with higher priority for Bully / ring order)
        String nodeId = args.length>0?args[0]:"A1";
        String group = args.length>1?args[1]:"A"; // A=gRPC, B=RMI
        int tcpPort = args.length>2?Integer.parseInt(args[2]):5000;
        String peersStr = args.length>3?args[3]:"";

        LamportClock clock = new LamportClock();
        SnapshotManager snapshot = new SnapshotManager(nodeId);

        // TCP intra-group server
        new Thread(new TcpServer(tcpPort)).start();

        // Multicast inter-group listener
        new Thread(new MulticastListener("230.0.0.0", 4446, snapshot)).start();

        // Group-specific middleware
        if (group.equals("B")) {
            try { LocateRegistry.createRegistry(1099); } catch (Exception ignored) {}
            RemoteNodeImpl impl = new RemoteNodeImpl(nodeId, clock, snapshot);
            java.rmi.Naming.rebind("//localhost/" + nodeId, impl);
            System.out.println("RMI node bound: " + nodeId);
        } else {
            // TODO: start gRPC server for Group A (GrpcNodeServer.start(nodeId, clock, snapshot))
            System.out.println("gRPC server placeholder for node " + nodeId);
        }

        // Parse peers for election
        List<String> peers = new ArrayList<>();
        if (!peersStr.isEmpty()) {
            peers.addAll(Arrays.asList(peersStr.split(",")));
        }

        // Election per group
        String leaderId;
        if (group.equals("A")) {
            BullyElection bully = new BullyElection(nodeId, peers);
            leaderId = bully.startElection();
        } else {
            RingElection ring = new RingElection(nodeId, peers);
            leaderId = ring.startElection();
        }
        System.out.println("Group " + group + " elected leader: " + leaderId);

        // Heartbeats to peers
        HeartbeatManager hb = new HeartbeatManager(group, peers);
        hb.start();

        // Leaders negotiate supercoordinator
        if (nodeId.equals(leaderId)) {
            new Thread(new GlobalCoordinator(leaderId, group)).start();
        }

        System.out.println("Node " + nodeId + " started. group=" + group + " tcpPort=" + tcpPort);
    }
}