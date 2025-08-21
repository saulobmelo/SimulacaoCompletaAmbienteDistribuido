package net;

import java.net.*;

public class MulticastListener implements Runnable {
    private final InetAddress grupo;
    private final int porta;
    private volatile boolean executando = true;

    public MulticastListener(String groupAddr, int porta) throws Exception {
        this.grupo = InetAddress.getByName(groupAddr); this.porta = porta;
    }

    public void stop() { executando = false; }

    @Override
    public void run() {
        try (MulticastSocket ms = new MulticastSocket(porta)) {
            ms.joinGroup(grupo);
            byte[] buf = new byte[1024];
            while (executando) {
                DatagramPacket p = new DatagramPacket(buf, buf.length);
                ms.receive(p);
                String msg = new String(p.getData(), 0, p.getLength());
                System.out.println("Mensagem multicast recebida: " + msg);
            }
            ms.leaveGroup(grupo);
        } catch (Exception e) {
            System.err.println("MulticastListener erro: " + e.getMessage());
        }
    }
}