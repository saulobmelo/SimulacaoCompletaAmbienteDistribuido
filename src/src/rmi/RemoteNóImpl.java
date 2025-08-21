package rmi;

import common.LamportClock;
import common.Message;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class RemoteNóImpl extends UnicastRemoteObject implements RemoteNó {
    private final String nodeId;
    private final LamportClock relogio;

    public RemoteNóImpl(String nodeId, LamportClock relogio) throws RemoteException {
        super();
        this.nodeId = nodeId; this.relogio = relogio;
    }

    @Override
    public void receiveMessage(Message m) throws RemoteException {
        relogio.updateOnReceive(m.lamport);
        System.out.println("Mensagem recebida via RMI: " + m.toString());
    }

    @Override
    public int getLamport() throws RemoteException { return relogio.getTime(); }

    @Override
    public void receiveMarker(String snapshotId, String fromNó) throws RemoteException {
        System.out.println("Received MARKER " + snapshotId + " origem " + fromNó);
        // snapshot logic implemented in SnapshotManager class
    }

    @Override
    public boolean heartbeat() throws RemoteException { return true; }

    @Override
    public Map<String, Object> requestState() throws RemoteException {
        Map<String,Object> estado = new HashMap<>();
        estado.put("nodeId", nodeId);
        estado.put("lamport", relogio.getTime());
        return estado;
    }
}