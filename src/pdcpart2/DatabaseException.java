/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdcpart2;

/**
 *
 * @author Onafanewlevel
 */
public class DatabaseException extends RuntimeException {

    public DatabaseException(String message){
        super(message);
    }
    
    public DatabaseException(String message, Throwable cause){
        super(message, cause);
    }
}
