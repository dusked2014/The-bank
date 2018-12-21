/*
 * Copyright (c) 18-11-19 下午7:44
 * 李高丞 @版权所有人
 * Version 1.0 Beta
 */

package bank.domain;

/**
 * In this class, we can create the checking account
 * and initialize the balance and the protective balance
 *
 * @author 李高丞
 * @version 1.0 Beat
 */
public class CheckingAccount extends Account {
    // The protective balance
    private double overdraftProtection;

    /**
     * Create a checking account
     * and deposit the money in the bank
     *
     * @param balance the money you deposit
     */
    public CheckingAccount(double balance) {
        super(balance);
        this.overdraftProtection = 0;
    }

    /**
     * Default constructor
     */
    public CheckingAccount() {

    }

    /**
     * Get the checking account overdraft protection
     * @return the overdraft protection
     */
    public double getOverdraftProtection() {
        return overdraftProtection;
    }

    /**
     * Create a checking account
     * and deposit the money in the bank
     * with the protective balance
     *
     * @param balance the money you deposit
     * @param protect the money you'll use in the emergency
     */
    public CheckingAccount(double balance, double protect) {
        super(balance);
        this.overdraftProtection = protect;
    }

    /**
     * Initialize the checking account protect money
     *
     * @param overdraftProtection the protect money
     */
    public void setOverdraftProtection(double overdraftProtection) {
        this.overdraftProtection = overdraftProtection;
    }

    /**
     * Withdraw some money from the bank
     *
     * @param amt the money you withdraw
     * @throws OverdraftException Overdraft protection
     */
    public void withdraw(double amt) throws OverdraftException {
        this.balance = this.balance - amt;

        // If the balance is insufficient
        if (this.balance < 0) {
            // If the overdraft protection account is not zero
            if (this.overdraftProtection != 0) {
                this.balance = this.balance + amt;

                if (this.overdraftProtection - amt > 0) {
                    double last = amt - this.balance;
                    this.balance = 0;
                    this.overdraftProtection = this.overdraftProtection - last;

                } else if ((this.overdraftProtection + this.balance) - amt == 0) {
                    this.balance = 0;
                    this.overdraftProtection = 0;
                    //throw new OverdraftException("Insufficient funds for overdraft protection", amt - this.balance);
                } else {
                    // if it can not paid, it wil throw an exception
                    this.overdraftProtection += amt;
                    throw new OverdraftException("Insufficient funds for overdraft protection", amt - this.balance);
                }
            } else {
                // If there is no overdraft protection and can't be paid,
                // it will also throw an exception
                this.balance = this.balance + amt;
                throw new OverdraftException("No overdraft protection", amt - this.balance);
            }
        }

    }

}
