package net;

import common.Message;

import java.io.*;
import java.net.*;

public class TcpServer implements Runnable {
    private final int porta;
    private volatile boolean executando = true;
    public TcpServer(int porta) { this.porta = porta; }

    public void stop() { executando = false; }

    @Override
    public void run() {
        try (ServerSocket servidor = new ServerSocket(porta)) {
            System.out.println("TCP Servidor escutando na porta " + porta);
            while (executando) {
                Socket s = servidor.accept();
                new Thread(() -> handleClient(s)).start();
            }
        } catch (IOException e) {
            System.err.println("TcpServer erro: " + e.getMessage());
        }
    }

    private void handleClient(Socket s) {
        try (ObjectInputStream in = new ObjectInputStream(s.getInputStream())) {
            Object obj = in.readObject();
            if (obj instanceof Message) {
                Message m = (Message) obj;
                System.out.println("Mensagem recebida via TCP: " + m);
            }
        } catch (Exception e) {
            System.err.println("handleClient erro: " + e.getMessage());
        } finally {
            try { s.close(); } catch (IOException ignored) {}
        }
    }
}