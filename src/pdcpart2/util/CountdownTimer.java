/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdcpart2.util;

/**
 * The CountdownTimer class manages a countdown timer for a quiz game.
 * It provides functionality to start a countdown from a specified number
 * of seconds and checks if the timer has run out. The timer stops if an
 * answer is provided before it reaches zero.
 *
 * Author: Setefano Muller
 * Author: Tharuka Rodrigo
 */

import pdcpart2.interfaces.TimerListener;
import pdcpart2.interfaces.TimeControl;
import javax.swing.*;
import pdcpart2.Messages;

public class CountdownTimer implements TimeControl {

    private Messages message;
    private boolean answered;
    private boolean timerRunOut;
    private JLabel countdownLabel;
    private Thread countdownThread;
    private TimerListener listener; // Listener to notify when timer expires

    /**
     * Constructor for CountdownTimer.
     *
     * @param answered       Indicates if the question has been answered.
     * @param countdownLabel The JLabel to display the remaining time.
     * @param listener       The TimerListener to notify when the timer expires.
     */
    public CountdownTimer(boolean answered, JLabel countdownLabel, TimerListener listener) {
        message = new Messages();
        this.answered = answered;
        this.timerRunOut = false;
        this.countdownLabel = countdownLabel;
        this.listener = listener;
    }

    @Override
    public void StartTimer() {
        countdownThread = new Thread(() -> {
            for (int i = 15; i > 0; i--) {  // Example: 15 seconds countdown
                if (answered) {
                    return;  // Exit the loop if the question is answered
                }

                // Update the JLabel with the remaining time on the EDT
                final int timeLeft = i;
                SwingUtilities.invokeLater(() -> countdownLabel.setText("Time left: " + timeLeft + " seconds"));

                try {
                    Thread.sleep(1000);  // Pause for 1 second
                } catch (InterruptedException e) {
                    // Thread interrupted; exit gracefully
                    return;
                }
            }

            if (!answered) {
                SwingUtilities.invokeLater(() -> {
                    message.timesUp(); // Show time's up message
                    if (listener != null) {
                        listener.timerExpired(); // Notify the listener
                    }
                });
                timerRunOut = true;
                answered = true;
            }
        });

        countdownThread.start();  // Start the countdown thread
    }

    @Override
    public void StopTimer() {
        answered = true;  // Stop the countdown when an answer is provided
        if (countdownThread != null && countdownThread.isAlive()) {
            countdownThread.interrupt();  // Interrupt the thread to stop it
        }
    }

//    public boolean hasTimerRunOut() {
//        return timerRunOut;
//    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
        if (answered) {
            timerRunOut = false;
        }
    }
}



