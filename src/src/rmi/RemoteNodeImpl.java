package rmi;

import common.LamportClock;
import common.Message;
import core.SnapshotManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class RemoteNodeImpl extends UnicastRemoteObject implements RemoteNode {
    private final String nodeId;
    private final LamportClock clock;
    private final SnapshotManager snapshot;

    public RemoteNodeImpl(String nodeId, LamportClock clock, SnapshotManager snapshot) throws RemoteException {
        super();
        this.nodeId = nodeId; this.clock = clock; this.snapshot = snapshot;
    }

    @Override
    public void receiveMessage(Message m) throws RemoteException {
        // increase lamport on receive
        // clock.receive(m.lamport);
        System.out.println(nodeId + " RMI received: " + m);
    }

    @Override
    public int getLamport() throws RemoteException {
        return clock.getTime();
    }

    @Override
    public void receiveMarker(String snapshotId, String fromNode) throws RemoteException {
        System.out.println(nodeId + " received MARKER " + snapshotId + " from " + fromNode);
        snapshot.receiveMarker(snapshotId, fromNode);
    }

    @Override
    public boolean heartbeat() throws RemoteException { return true; }

    @Override
    public Map<String, Object> requestState() throws RemoteException {
        Map<String,Object> state = new HashMap<>();
        state.put("nodeId", nodeId);
        state.put("lamport", clock.getTime());
        return state;
    }
}