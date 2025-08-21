package rmi;

import common.Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface RemoteNó extends Remote {
    void receiveMessage(Message m) throws RemoteException;
    int getLamport() throws RemoteException;
    void receiveMarker(String snapshotId, String fromNó) throws RemoteException;
    boolean heartbeat() throws RemoteException;
    Map<String,Object> requestState() throws RemoteException; // for snapshot/estado transfer
}