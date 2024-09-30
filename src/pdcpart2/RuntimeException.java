/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pdcpart2;

/**
 *
 * @author Onafanewlevel
 */
public class RuntimeException extends Throwable {

    private String message;

    public RuntimeException(String message) {
        this.message = message;
    }

    public RuntimeException(String message, Throwable cause) {
        super(cause);  // Pass the cause to the superclass constructor
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}