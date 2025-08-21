package net;

import common.Message;

import java.io.*;
import java.net.*;

public class TcpClient {
    public static boolean send(String host, int porta, Message m) {
        try (Socket s = new Socket(host, porta);
             ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream())) {
            out.writeObject(m);
            out.flush();
            return true;
        } catch (IOException e) {
            System.err.println("TcpClient send erro: " + e.getMessage());
            return false;
        }
    }
}