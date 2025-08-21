package rmi;

import common.LamportClock;
import common.Message;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

public class RemoteNodeImpl extends UnicastRemoteObject implements RemoteNode {
    private final String nodeId;
    private final LamportClock clock;

    protected RemoteNodeImpl(String nodeId, LamportClock clock) throws RemoteException {
        super();
        this.nodeId = nodeId; this.clock = clock;
    }

    @Override
    public void receiveMessage(Message m) throws RemoteException {
        clock.updateOnReceive(m.lamport);
        System.out.println("RMI Received: " + m.toString());
    }

    @Override
    public int getLamport() throws RemoteException { return clock.getTime(); }

    @Override
    public void receiveMarker(String snapshotId, String fromNode) throws RemoteException {
        System.out.println("Received MARKER " + snapshotId + " from " + fromNode);
        // Snapshot logic implemented in SnapshotManager class
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