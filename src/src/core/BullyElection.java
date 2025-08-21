package core;

import java.util.*;

public class BullyElection {
    private final String myId;
    private final List<String> higherPeers; // higher priority peers (IDs greater than me)

    public BullyElection(String myId, List<String> higherPeers) {
        this.myId = myId; this.higherPeers = higherPeers;
    }

    public String startElection() {
        // Simplified: if any higher peer responds (assume alive), it wins; else I win
        for (String p : higherPeers) {
            // In a real impl, send ELECTION message and wait for OK
            // Here we sort and pick the lexicographically max as winner
        }
        List<String> all = new ArrayList<>(higherPeers);
        all.add(myId);
        all.sort(Comparator.naturalOrder());
        return all.get(all.size()-1);
    }
}