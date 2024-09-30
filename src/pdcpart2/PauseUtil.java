/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdcpart2;

/**
 *
 * @author Onafanewlevel
 */
public class PauseUtil {
    public static void pause(int ms){
        try{
            Thread.sleep(ms);
        } catch(InterruptedException e){}
    }
    
}
