/*
 *  ClassName   CustomerReport
 *  CopyRight   李高丞.All Rights Reserved.
 *  Version     1.0 2018.11.04
 */

package bank.reports;

import bank.domain.*;

import java.text.NumberFormat;

/**
 * In this class, we'll print some customers message
 * about their account, show them how much money they
 * still has.
 *
 * @author 李高丞
 * @version 1.0 Beat
 */
public class CustomerReport {
    public void generateReport() {

        NumberFormat currency_format = NumberFormat.getCurrencyInstance();
        Bank bank = Bank.getBank();
        Customer customer;

        // Sort by the lexigraphical order
        bank.sortCustomers();

        System.out.println("\t\t\tCUSTOMERS REPORT");
        System.out.println("\t\t\t================");

        for (int cust_idx = 0; cust_idx < bank.getNumOfCustomers(); cust_idx++) {
            customer = bank.getCustomer(cust_idx);

            System.out.println();
            System.out.println("Customer: "
                    + customer.getName());

            for (int acct_idx = 0; acct_idx < customer.getNumOfAccounts(); acct_idx++) {
                Account account = customer.getAccount(acct_idx);
                String account_type = "";

                // Determine the account type
                /*** Step 1:
                 **** Use the instanceof operator to test what type of account
                 **** we have and set account_type to an appropriate value, such
                 **** as "Savings Account" or "Checking Account".
                 ***/
                if (account instanceof SavingsAccount) {
                    account_type = "Saving Account";
                } else if (account instanceof CheckingAccount) {
                    account_type = "Checking Account";
                }

                // Print the current balance of the account
                /*** Step 2:
                 **** Print out the type of account and the balance.
                 **** Feel free to use the currency_format formatter
                 **** to generate a "currency string" for the balance.
                 ***/
                System.out.println(account_type + ":current balance is "
                        + currency_format.format(account.getBalance()));

            }
        }
    }
}
