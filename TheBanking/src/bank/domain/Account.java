/*
 * Copyright (c) 18-11-19 下午7:49
 * 李高丞 @版权所有人
 * Version 1.0 Beta
 */

package bank.domain;

import Data.FindClient;

import javax.swing.*;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * In the Account class, we initialize the customer's account
 * we packed some methods, which can use safety and convenience
 * We can create a customer's account and initialize the money
 * <p>
 * now it contain one static method, which can save the account
 * message to the file
 *
 * @author 李高丞
 * @version 1.0 Beat
 */
public class Account {
    // The balance of every customer's account
    protected double balance;

    /**
     * The default constructor
     */
    public Account() {

    }

    /**
     * The constructor that can initialize the balance of
     * an account
     *
     * @param init_balance the beginning money of an account
     */
    public Account(double init_balance) {
        this.balance = init_balance;
    }

    /**
     * Get the current money in the account
     *
     * @return the current money
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Initialize the balance
     *
     * @param balance the account balance
     */
    public void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * Deposit some money in the bank
     *
     * @param amt the money you deposit
     * @return if success, it'll return true
     */
    public boolean deposit(double amt) {
        this.balance = this.balance + amt;
        return true;
    }

    /**
     * Withdraw some money from the bank,
     * if withdraw money more than in the bank,
     * it will throw an OverdraftException
     *
     * @param amt the money you withdraw
     * @throws OverdraftException Overdraft protection
     */
    public void withdraw(double amt) throws OverdraftException {
        this.balance -= amt;

        if (this.balance < 0) {
            this.balance = this.balance + amt;
            throw new OverdraftException("Insufficient funds for overdraft protection", amt - this.balance);
            //System.out.println("Exception: Insufficient funds for overdraft protection");
        }

    }

    /**
     * Save the account to the file
     *
     * @param customers    the customers list
     * @param customerName the customer name that need to be saved
     */
    public static void saveAccount(List<Customer> customers, String customerName) {
        File file;
        List<Account> accounts = null;
        OutputStreamWriter osw = null;
        FileOutputStream fos;

        // find the account file
        file = FindClient.findAccount(customerName);

        // fine the account messgae
        for (Customer customer : customers)
            if (customer.getName().equals(customerName))
                accounts = customer.getAccounts();


        try {
            fos = new FileOutputStream(file);
            osw = new OutputStreamWriter(fos);
            String str = "";

            int type = 0; // judge the write type

            // if the client account list is null, it don't need to write to file
            if (accounts == null) return;

            if (accounts.size() == 0) return;

            // write the account
            for (Account account : accounts) {

                // if the type is 0, it means that the current account type is 'Checking Account'
                // if the type is 1, it means that the current account type is 'Saving Acconnt'

                if (type == 0) {

                    // the write string type
                    str += "Balance " + account.getBalance() +
                            " OverDraft " + ((CheckingAccount) account).getOverdraftProtection()
                            + "\n";
                }
                if (type == 1) {

                    // the write string type
                    str += "Balance " + account.getBalance() +
                            " Interest " + ((SavingsAccount) account).getInterestRate() +
                            "\n";
                }
                type++;
            }

            // write the string to the file
            osw.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                osw.flush();
                osw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
