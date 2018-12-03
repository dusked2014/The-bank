/*
 * Copyright (c) 18-11-19 下午7:44
 * 李高丞 @版权所有人
 * Version 1.0 Beta
 */

package bank.domain;

import Data.FindClient;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * This class extend Banking project by adding a Customer class
 * In Customer class, it contain one Account object
 * <p>
 * and now it contain three static methods, which is load the accout message from
 * the file, and another is get the customer message from the file, and the last
 * one is save the customer message to the file.
 *
 * @author Li Gaocheng
 * @version 1.0 Beat
 */
public class Customer implements Comparable {

    // The basic message of the customer
    private String name;
    private String password;
    private LinkedList<Account> accounts;

    /**
     * Create a customer
     *
     * @param name the name of customer
     * @param password  the user password
     */
    public Customer(String name, String password) {
        this.name = name;
        this.password = password;
        this.accounts = new LinkedList<>();
    }

    /**
     * Create a customer
     *
     * @param name the name of customer
     */
    public Customer(String name) {
        this.name = name;

    }

    /**
     * Get the customer name
     *
     * @return the customer name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the customer name
     *
     * @param name the name that changed
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the customer password
     *
     * @return the client password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the client password
     *
     * @param password the password tha changed
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the customer account list
     *
     * @return the list that contain the cutomer account message
     */
    public LinkedList<Account> getAccounts() {
        return accounts;
    }

    /**
     * Set customer's account
     *
     * @param accounts a hole new account
     */
    public void setAccounts(LinkedList<Account> accounts) {
        this.accounts = accounts;
    }

    /**
     * Add a customer's account in the ArrayList
     *
     * @param account the customer's account
     */
    public void addAccount(Account account) {
        accounts.add(accounts.size(), account);
    }

    /**
     * Get the customer's account from the ArrayList
     *
     * @param index the number of the account placed in the ArrayList
     * @return the customer's account
     */
    public Account getAccount(int index) {
        return accounts.get(index);
    }

    /**
     * Get all the customer's accounts
     *
     * @return number of the customer's accounts
     */
    public int getNumOfAccounts() {
        if (accounts == null) return 0;
        return accounts.size();
    }

    /**
     * Implementation of Comparable interface
     * Override the compareTo method and compare in alphabetic order
     * Compare lastName first and then firstName
     *
     * @param o The object need to compare
     * @return 1 for who call the method bigger,0 for equal, -1 for smaller
     */
    @Override
    public int compareTo(Object o) {
        return this.name.compareTo(((Customer) o).getName());
    }

    /**
     * Load the account message to the linked list
     *
     * @param clientName the client name that need to search
     * @return the linked list that contain the customer account message
     */
    public static LinkedList<Account> loadAccountMsg(String clientName) {

        // find the client account file
        File checkFile = FindClient.findAccount(clientName);
        BufferedReader br;
        LinkedList<Account> clientAccount = new LinkedList<>();

        try {
            br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(checkFile)));
            String str;
            int type = 0; // judge the read account type
            while ((str = br.readLine()) != null) {

                // split the message with a space
                String[] strs = str.split(" ");
                if (strs.length < 4)
                    System.out.println("Wrong type: " + str);

                // if type is 0, it means that current read account type is 'Checking Account'
                // if type is 1, it means that current read account type is 'Saving Account'
                if (type == 0) {

                    // save the message to the 'Checking Account'
                    if (!strs[1].equals("null") && !strs[3].equals("null")) {
                        clientAccount.add(new CheckingAccount
                                (Double.parseDouble(strs[1]), Double.parseDouble(strs[3])));
                    } else if (strs[1].equals("null")) {
                        clientAccount.add(new CheckingAccount());
                    } else if (strs[3].equals("null")) {
                        clientAccount.add(new CheckingAccount(Double.parseDouble(strs[1])));
                    }
                }
                if (type == 1) {

                    // save the message to the 'Saving Account'
                    if (!strs[1].equals("null") && !strs[3].equals("null")) {
                        clientAccount.add(new SavingsAccount
                                (Double.parseDouble(strs[1]), Double.parseDouble(strs[3])));
                    } else if (strs[1].equals("null")) {
                        clientAccount.add(new SavingsAccount());
                    } else if (strs[3].equals("null")) {
                        clientAccount.add(new SavingsAccount(Double.parseDouble(strs[1])));
                    }
                }

                type++;
            }

        } catch (IOException e) {
            System.out.println("Cannot find the client.");
        }

        return clientAccount;
    }

    /**
     * Get the customerList
     *
     * @return the customer list
     */
    public static LinkedList<Customer> getCustomersList() {
        LinkedList<Customer> list = new LinkedList<>();

        // find the client message
        File file = new File("C:\\Users\\hasee\\Documents\\JavaCode\\" +
                "The Banking\\ID&Psd.txt");

        try {

            // read the file
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file)));
            String str;
            while ((str = br.readLine()) != null) {

                // split the str with '='
                String[] strs = str.split("=");
                if (strs.length != 2)
                    System.out.println("Wrong type: " + str);

                // add the custmer ID and password into the customer
                Customer customer = new Customer(strs[0], strs[1]);

                // load the account message
                Customer.loadAccountMsg(customer.getName());

                // add a new customer to the list
                list.add(customer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // load the account message
        for (Customer customer : list) {
            customer.setAccounts(loadAccountMsg(customer.getName()));
        }

        // sort the customer
        list.sort(Customer::compareTo);

        return list;
    }

    /**
     * Save the customer message to the file
     *
     * @param customers the customer list
     */
    public static void saveCustomer(List<Customer> customers) {

        // find the client message
        File file = new File("C:\\Users\\hasee\\Documents\\JavaCode\\" +
                "The Banking\\ID&Psd.txt");

        OutputStreamWriter osw = null;
        FileOutputStream fos;

        try {

            // write the file
            fos = new FileOutputStream(file);
            osw = new OutputStreamWriter(fos);
            String str = "";

            for (int i = 0; i < customers.size(); i++) {

                // write to the file string type
                str += customers.get(i).getName() + "=" +
                        customers.get(i).getPassword() + "\n";
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
