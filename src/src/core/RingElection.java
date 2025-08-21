package core;

import java.util.List;

public class RingElection {
    private final String myId;
    private final List<String> ringOrder; // next index is next node

    public RingElection(String myId, List<String> ringOrder) {
        this.myId = myId; this.ringOrder = ringOrder;
    }

    public String startElection() {
        System.out.println(myId + " initiating ring election");
        // pass token with IDs, return elected
        return null;
    }
}