package net;

import common.Message;

import java.io.*;
import java.net.*;

public class TcpServer implements Runnable {
    private final int port;
    private volatile boolean running = true;
    public TcpServer(int port) { this.port = port; }

    public void stop() { running = false; }

    @Override
    public void run() {
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("TCP Server listening on " + port);
            while (running) {
                Socket s = server.accept();
                new Thread(() -> handleClient(s)).start();
            }
        } catch (IOException e) {
            System.err.println("TcpServer error: " + e.getMessage());
        }
    }

    private void handleClient(Socket s) {
        try (ObjectInputStream in = new ObjectInputStream(s.getInputStream())) {
            Object obj = in.readObject();
            if (obj instanceof Message) {
                Message m = (Message) obj;
                System.out.println("TCP Received: " + m);
            }
        } catch (Exception e) {
            System.err.println("handleClient error: " + e.getMessage());
        } finally {
            try { s.close(); } catch (IOException ignored) {}
        }
    }
}