/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package comp603projectpart2;

/**
 *
 * @author setefanomuller
 */
public class TimeUtil {

    /**
     * Pauses the execution of the program for a specified number of
     * milliseconds. This method uses Thread.sleep to create a delay in the
     * game, which can be used to enhance the user experience by adding dramatic
     * pauses or allowing time for the player to read the information displayed
     * on the screen.
     *
     * @param milliseconds The number of milliseconds to pause the program.
     */
    public static void pause(int milliseconds) {
        try {
            Thread.sleep(milliseconds); // Pause the current thread for the specified duration
        } catch (InterruptedException e) {
        }
    }
}
