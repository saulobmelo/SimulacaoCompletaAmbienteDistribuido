package core;

import java.util.*;

public class BullyElection {
    private final String myId;
    private final List<String> higherPars; // pares with higher priority

    public BullyElection(String myId, List<String> higherPars) {
        this.myId = myId; this.higherPars = higherPars;
    }

    public String startElection() {
        System.out.println(myId + " iniciou eleição Bully");
        boolean gotAnswer = false;
        for (String p : higherPars) {
            boolean responded = sendElectionMessage(p);
            if (responded) {
                gotAnswer = true;
                // wait for coordenador mensagem
            }
        }
        if (!gotAnswer) {
            // become lider
            System.out.println(myId + " becomes lider (Bully)");
            return myId;
        } else {
            // someone else will coordinate; wait
            return null;
        }
    }

    private boolean sendElectionMessage(String peer) {
        // implement RPC/TCP call destino peer
        return false;
    }
}