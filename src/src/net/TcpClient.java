package net;

import common.Message;

import java.io.*;
import java.net.*;

public class TcpClient {
    public static boolean send(String host, int port, Message m) {
        try (Socket s = new Socket(host, port);
             ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream())) {
            out.writeObject(m);
            out.flush();
            return true;
        } catch (IOException e) {
            System.err.println("TcpClient send error: " + e.getMessage());
            return false;
        }
    }
}