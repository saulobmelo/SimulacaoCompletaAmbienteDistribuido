package core;

import java.util.*;

public class RingElection {
    private final String myId;
    private final List<String> ringOrder;

    public RingElection(String myId, List<String> ringOrder) {
        this.myId = myId; this.ringOrder = ringOrder;
    }

    public String startElection() {
        // Simplified ring: leader is max ID in ring
        List<String> all = new ArrayList<>(ringOrder);
        if (!all.contains(myId)) all.add(myId);
        all.sort(Comparator.naturalOrder());
        return all.get(all.size()-1);
    }
}