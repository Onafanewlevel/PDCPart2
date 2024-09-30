package pdcpart2;

public class CountdownUtil {
    private Player player;
    private int seconds;
    private Thread countdownThread;
    private volatile boolean isRunning;

    public CountdownUtil(int seconds, Player player) {
        this.seconds = seconds;
        this.player = player;
    }

    public void startCountdown() {
        isRunning = true;
        countdownThread = new Thread(() -> {
            try {
                for (int i = seconds; i >= 0; i--) {
                    if (!isRunning) {
                        System.out.println("Countdown stopped.");
                        return;
                    }
                    if (player.hasGivenAnswer()) {
                        stopCountdown();
                        return;
                    }
                    System.out.println(i);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                System.out.println("Countdown was interrupted.");
                Thread.currentThread().interrupt(); // Restore interrupted status
            }
        });
        countdownThread.start();
    }

    public void stopCountdown() {
        isRunning = false;
        if (countdownThread != null && countdownThread.isAlive()) {
            countdownThread.interrupt();
        }
    }
}
