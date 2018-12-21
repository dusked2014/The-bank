/*
 *  ClassName   SavingsAccount
 *  CopyRight   李高丞.All Rights Reserved.
 *  Version     1.0 2018.11.04
 */

package bank.domain;

/**
 * In this class, we can create the savings account
 * and initialize the balance and interest rate
 *
 * @author 李高丞
 * @version 1.0 Beat
 */
public class SavingsAccount extends Account {
    // The interest per year
    private double interestRate;

    /**
     * Create a saving account which can set the balance
     * and the interest rate
     *
     * @param balance       the money you deposit
     * @param interest_rate the interest rate
     */
    public SavingsAccount(double balance, double interest_rate) {
        super(balance);
        this.interestRate = interest_rate;
    }

    /**
     * Default construction
     */
    public SavingsAccount() {

    }

    /**
     * Create a saving account which can set the balance
     *
     * @param balance the money you deposit
     */
    public SavingsAccount(double balance) {
        this.balance = balance;
    }

    /**
     * Get the saving account interest rate
     *
     * @return the interest rate
     */
    public double getInterestRate() {
        return interestRate;
    }

    /**
     * Initialize the saving account interest rate
     *
     * @param interestRate the saving account interest rate
     */
    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }
}
