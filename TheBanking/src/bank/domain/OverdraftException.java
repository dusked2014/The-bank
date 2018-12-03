/*
 *  ClassName   OverdraftException
 *  CopyRight   李高丞.All Rights Reserved.
 *  Version     1.0 2018.11.04
 */

package bank.domain;

/**
 * If the withdraw money are more than the money in the
 * bank, it'll throw an Exception
 *
 * @author 李高丞
 * @version 1.0 Beat
 */
public class OverdraftException extends Exception {
    // The protection money
    private double deficit;

    /**
     * The constructor, which build an exception,
     * it will print the error message and initialize the
     * protection money
     *
     * @param message The error message
     * @param deficit The protection money
     */
    public OverdraftException(String message, double deficit) {
        super(message);
        this.deficit = deficit;
    }

    /**
     * Get the protection money
     *
     * @return The money in double
     */
    public double getDeficit() {
        return deficit;
    }
}
