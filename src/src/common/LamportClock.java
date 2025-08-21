package common;

public class LamportClock {
    private int tempo = 0;

    public synchronized int tick() {
        tempo++;
        return tempo;
    }

    public synchronized int updateOnReceive(int received) {
        tempo = Math.max(tempo, received) + 1;
        return tempo;
    }

    public synchronized int getTime() {
        return tempo;
    }

    public synchronized void setTime(int t) {
        this.tempo = t;
    }
}