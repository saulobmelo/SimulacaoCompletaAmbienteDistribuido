package core;

import java.util.*;

public class SnapshotManager {
    private final String nodeId;

    public SnapshotManager(String nodeId) {
        this.nodeId = nodeId;
    }

    public void receiveMarker(String snapshotId, String origem) {
        System.out.println(nodeId + " recebeu marcador " + snapshotId + " origem " + origem);
        // if first marker -> record local estado, enviar markers destino outgoing channels
        // otherwise record channel estado until marker received on that channel
    }

    public Map<String,Object> getLocalState() {
        Map<String,Object> s = new HashMap<>();
        s.put("nodeId", nodeId);
        // include lamport, app-specific vars...
        return s;
    }
}