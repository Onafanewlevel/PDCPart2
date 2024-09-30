package pdcpart2;



public class CountdownUtil implements TimeControl, Runnable {
    private Player player;
    private int seconds;
    private Thread countdownThread;
    private volatile boolean isRunning;

    public CountdownUtil(int seconds, Player player) {
        this.seconds = seconds;
        this.player = player;
    }
    
    @Override
    public void run() {
        try {
            for (int i = seconds; i >= 0; i--) {
                if (!isRunning) {
                    System.out.println("Countdown stopped.");
                    return;
                }
                if (player.hasGivenAnswer()) {
                    stopTimer();
                    return;
                }
                System.out.println(i);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.out.println("Countdown was interrupted.");
            Thread.currentThread().interrupt(); // Restore interrupted status
        }
    }
    
    @Override
    public void startTimer() {
        isRunning = true;
        countdownThread = new Thread(this); // Use the Runnable implementation
        countdownThread.start();
    }

    @Override
    public void stopTimer() {
        isRunning = false;
        if (countdownThread != null && countdownThread.isAlive()) {
            countdownThread.interrupt();
        }
    }
}

