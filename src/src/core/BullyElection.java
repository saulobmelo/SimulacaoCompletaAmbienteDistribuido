package core;

import java.util.*;

public class BullyElection {
    private final String myId;
    private final List<String> higherPeers; // peers with higher priority

    public BullyElection(String myId, List<String> higherPeers) {
        this.myId = myId; this.higherPeers = higherPeers;
    }

    public String startElection() {
        System.out.println(myId + " started Bully election");
        boolean gotAnswer = false;
        for (String p : higherPeers) {
            boolean responded = sendElectionMessage(p);
            if (responded) {
                gotAnswer = true;
                // wait for coordinator message
            }
        }
        if (!gotAnswer) {
            // become leader
            System.out.println(myId + " becomes leader (Bully)");
            return myId;
        } else {
            // someone else will coordinate; wait
            return null;
        }
    }

    private boolean sendElectionMessage(String peer) {
        // implement RPC/TCP call to peer
        return false;
    }
}