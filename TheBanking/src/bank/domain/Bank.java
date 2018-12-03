/*
 * Copyright (c) 18-11-19 下午7:47
 * 李高丞 @版权所有人
 * Version 1.0 Beta
 */

package bank.domain;

import java.util.LinkedList;

/**
 * In the Bank class, we have a ArrayList of ten Customers,
 * we can add a new Customer, we can named it,
 * and we can get how many Customer in the ArrayList
 *
 * @author 李高丞
 * @version 1.0 Beat
 */
public class Bank {

    // Create the ArrayList of Customer objects
    private LinkedList<Customer> customers;
    private static Bank bankInstance = new Bank();

    /**
     * Initializes the customers ArrayList with some appropriate maximum size
     */
    private Bank() {
        customers = new LinkedList<>();
    }

    /**
     * Get the bank instance
     *
     * @return the instance of bank
     */
    public static Bank getBank() {
        return bankInstance;
    }

    /**
     * This method construct a new Customer object
     * from the parameters (first name, last name)
     * and place it on the customers ArrayList
     *
     * @param name the name of customer
     * @param password the user password
     */
    public void addCustomer(String name, String password) {
        Customer customer = new Customer(name, password);
        customers.add(customers.size(), customer);
    }

    /**
     * Get the number of the customers
     *
     * @return An integer of the customer
     */
    public int getNumOfCustomers() {
        return customers.size();
    }

    /**
     * This method returns the customer associated with
     * the given index parameter
     *
     * @param index the position of customer
     * @return the customer
     */
    public Customer getCustomer(int index) {
        return customers.get(index);
    }

    /**
     * Get the customer
     *
     * @param name the customer name that need to search
     * @return if find it return the customer you search, else it return a new customer
     */
    public Customer getCustomer(String name) {
        int size = customers.size();

        for (int i = 0; i < size; i++) {
            if (name.equals(customers.get(i).getName()))
                return customers.get(i);
        }
        return new Customer("000000", "000000");
    }

    /**
     * Using the Customer class override method compareTo
     * sort by lexigraphical order
     */
    public void sortCustomers() {
        customers.sort(Customer::compareTo);
    }

}
