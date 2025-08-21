package core;

import java.util.*;

public class SnapshotManager {
    private final String nodeId;

    public SnapshotManager(String nodeId) {
        this.nodeId = nodeId;
    }

    public void receiveMarker(String snapshotId, String from) {
        System.out.println(nodeId + " received marker " + snapshotId + " from " + from);
        // if first marker -> record local state, send markers to outgoing channels
        // otherwise record channel state until marker received on that channel
    }

    public Map<String,Object> getLocalState() {
        Map<String,Object> s = new HashMap<>();
        s.put("nodeId", nodeId);
        // include lamport, app-specific vars...
        return s;
    }
}