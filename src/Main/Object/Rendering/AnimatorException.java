/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main.Object.Rendering;

/**
 * Exception for animator.
 *
 * @author Raymond Gao
 */
public class AnimatorException extends Throwable {
    /**
     * Custom error message, gives more detail
     */
    private String message;

    /**
     * Constructor with message
     * @param message custom error message
     */
    public AnimatorException(String message) {
        super();
        this.message = message;
    }
    
    /**
     * Returns the custom error message
     * @return 
     */
    public String getMessage() {
        return message;
    }
    
    /**
     * Appends to the custom error message, useful if you want to give 
     * information outside the animator ex, which object owns the animator.
     * @param message string to append to error message
     */
    public void appendToErrorMessage(String message) {
        this.message += " " + message;
    }
}
