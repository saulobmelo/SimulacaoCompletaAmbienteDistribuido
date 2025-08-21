package common;

public class LamportClock {
    private int time = 0;

    public synchronized int tick() {
        time++;
        return time;
    }

    public synchronized int updateOnReceive(int received) {
        time = Math.max(time, received) + 1;
        return time;
    }

    public synchronized int getTime() {
        return time;
    }

    public synchronized void setTime(int t) {
        this.time = t;
    }
}