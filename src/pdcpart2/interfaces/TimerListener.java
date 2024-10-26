/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package pdcpart2.interfaces;

/**
 * TimerListener interface defines a callback method to be invoked when the countdown timer expires.
 * This allows classes implementing this interface to respond to the timer expiration event.
 */
public interface TimerListener {
    /**
     * This method is called when the countdown timer reaches zero.
     */
    void timerExpired();
}
