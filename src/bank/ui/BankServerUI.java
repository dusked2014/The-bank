/*
 * Copyright (c) 18-11-19 下午3:50
 * 李高丞 @版权所有人
 * Version 1.0 Beta
 */

package bank.ui;

import Data.DeleteFile;
import bank.domain.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

/**
 * This class is the graphical user interface of 'BankServerUI'.
 * It draws the graphical user interface and adds a series of event listeners,
 * but it still has some hidden problems that have not been solved.
 * <p>
 * BankServer GUI support manager of bank for account operations,
 * including : create, delete , query , modify and display all customers
 * information and account information; and the modified results can be saved
 * back to the data file
 *
 * @author 李高丞
 * @version 1.0 Beta
 */
public class BankServerUI extends JFrame {
    // using the singleton design patten
    private static Bank bank = Bank.getBank();

    // initialize the customer list
    private static List<Customer> customers = Customer.getCustomersList();

    // create the account list
    private static List<Account> accounts;

    // create the BankServerUI and using the singleton design patten
    private static BankServerUI serverUI = new BankServerUI();

    // create the panels for graphical user interface
    private JPanel westPanel, centerPanel, eastPanel, topPanel, middlePanel,
            mcPanel, bottomPanel, bwPanel, bcPanel;

    // create the buttons for graphical user interface
    private JButton accountButton, customerButton, deleteAccountButton,
            deleteCustomerButton, changePasswordButton, saveAccountButton,
            exitButton;

    // create the labels for graphical user interface
    private JLabel typeLabel, balanceLabel, overDraftLabel, interestLabel;

    // create the user list
    private JList<String> list;

    // create the account type comboBox
    private JComboBox<String> comboBox;

    // create the message JTextArea show the operation log
    private JTextArea msgArea, messageArea;

    // create the fields for typing the number
    private JTextField balanceField, overDraftField, interestField;

    // create the scroll pane for better use
    private JScrollPane scrollPane, msgPane;

    /**
     * Default constructor
     */
    private BankServerUI() {

    }

    /**
     * Get the 'BankServerUI' variable
     *
     * @return the BankServerUI variable
     */
    public static BankServerUI getServerUI() {
        return serverUI;
    }

    /**
     * A test to show the GUI
     *
     * @param args command line parameter
     */
    public static void main(String[] args) {
        BankServerUI ui = getServerUI();
        ui.lunchGUI();
        //DeleteFile.deleteCustomer("777");
        //File file = new File("C:\\Users\\hasee\\Documents\\JavaCode\\The Banking\\src\\Data\\Client\\777\\Account.txt");
        //System.out.println(file.getAbsoluteFile().delete());
        //accounts = Customer.loadAccountMsg(customers.get(1).getName());
        //System.out.println(((CheckingAccount) accounts.get(0)).getBalance());
        //System.out.println(((CheckingAccount) accounts.get(0)).getOverdraftProtection());
        //System.out.println(customers.get(0).getName());
        //System.out.println(customers.get(0).getAccount(0).getBalance());
        //System.out.println(((CheckingAccount)customers.get(1).getAccount(0)).getOverdraftProtection());
        //Account.saveAccount(customers);
        //customers.get(3).addAccount(new CheckingAccount());
        //customers.get(3).addAccount(new SavingsAccount());
        //System.out.println(customers.get(3).getAccount(0).getBalance());
        //Account.saveAccount(customers, customers.get(3).getName());
        //System.out.println((customers.get(0).getAccounts() == null));
    }

    /**
     * Start the 'BankServerUI' interface
     */
    public void lunchGUI() {
        // create the west panel
        westPanel = new JPanel();

        // create the center panel
        centerPanel = new JPanel();

        // create the east panel
        eastPanel = new JPanel();

        // set the server UI size and the layout manager
        serverUI.setPreferredSize(new Dimension(750, 500));
        serverUI.setLayout(new BorderLayout());

        // set the center panel layout manager
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));


        // add the client name to the JList
        String[] nameList = new String[customers.size()];
        Vector<String> temp = new Vector<>(customers.size());
        for (Customer customer : customers)
            temp.add(customer.getName());

        temp.toArray(nameList);
        list = new JList<>(nameList);
        list.addListSelectionListener(new listListener());

        // add the scroll
        scrollPane = new JScrollPane(list);

        // set the scrollPane size
        scrollPane.setPreferredSize(new Dimension(120, 450));

        // only show the vertical scrollbar
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // add title border
        scrollPane.setBorder(BorderFactory.createTitledBorder("Client : "));
        westPanel.add(scrollPane);

        // create the center panel's top panel
        topPanel = new JPanel();

        // set the top panel size
        topPanel.setPreferredSize(new Dimension(0, 100));

        // Instantiation of JTestArea
        // This JTextArea is used to display operation information
        messageArea = new JTextArea();

        // the operation information cannot change
        messageArea.setEditable(false);

        // set the message area font
        messageArea.setFont(new Font("Arial",Font.BOLD,16));

        // add the scrollPane
        scrollPane = new JScrollPane(messageArea);

        // set the scrollPane size
        scrollPane.setPreferredSize(new Dimension(300, 100));

        // only show the vertical scrollbar
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // add title border
        scrollPane.setBorder(BorderFactory.createTitledBorder("Message : "));
        topPanel.add(scrollPane);


        // create the middle panel
        middlePanel = new JPanel();

        // create the middle center panel
        mcPanel = new JPanel();

        // set the middle size and layout manager
        middlePanel.setPreferredSize(new Dimension(300, 140));
        mcPanel.setPreferredSize(new Dimension(290, 140));

        // set the middle center panel layout manager
        mcPanel.setLayout(new GridLayout(4, 2));

        // add title border for the middle center panel
        mcPanel.setBorder(BorderFactory.createTitledBorder("Account info : "));

        // client account message
        typeLabel = new JLabel("Type : ");
        balanceLabel = new JLabel("Balance : ");
        overDraftLabel = new JLabel("OverDraft : ");
        interestLabel = new JLabel("Interest : ");

        // set the label font
        typeLabel.setFont(new Font("Arial",Font.ITALIC,14));
        balanceLabel.setFont(new Font("Arial",Font.ITALIC,14));
        overDraftLabel.setFont(new Font("Arial",Font.ITALIC,14));
        interestLabel.setFont(new Font("Arial",Font.ITALIC,14));

        // account type
        comboBox = new JComboBox<>(new String[]{"Checking", "Saving"});
        balanceField = new JTextField();
        overDraftField = new JTextField();
        interestField = new JTextField();

        // first start the BankServerUI it's unmodifiable
        balanceField.setEditable(false);
        overDraftField.setEditable(false);
        interestField.setEditable(false);

        // add an action listener to the comboBox
        comboBox.addActionListener(new comboBoxListener());

        // add the key listener to the field
        // and only input number
        balanceField.addKeyListener(new voteElectyKeyListener());
        overDraftField.addKeyListener(new voteElectyKeyListener());
        interestField.addKeyListener(new voteElectyKeyListener());

        // add them to the middle panel
        mcPanel.add(typeLabel);
        mcPanel.add(comboBox);
        mcPanel.add(balanceLabel);
        mcPanel.add(balanceField);
        mcPanel.add(overDraftLabel);
        mcPanel.add(overDraftField);
        mcPanel.add(interestLabel);
        mcPanel.add(interestField);
        middlePanel.add(mcPanel);

        // create the center panel's bottom panel
        bottomPanel = new JPanel();

        // set the bottom panel's west panel layout manager
        GridLayout gridLayout = new GridLayout(4, 1);

        // setting vertical indentation for grid layout
        gridLayout.setVgap(10);

        // create the bottom panel's west panel
        bwPanel = new JPanel(gridLayout);

        // create the bottom panel's center panel
        // set up the layout manager
        bcPanel = new JPanel(new GridLayout(5, 1));

        // set their size
        bottomPanel.setPreferredSize(new Dimension(300, 160));
        bwPanel.setPreferredSize(new Dimension(130, 150));
        bcPanel.setPreferredSize(new Dimension(130, 150));

        /*
        Create a series of buttons
         */
        accountButton = new JButton("Add Account");
        accountButton.addActionListener(new addAccountListener());

        customerButton = new JButton("Add Customer");
        customerButton.addActionListener(new addCustomerListener());

        deleteAccountButton = new JButton("Delete Account");
        deleteAccountButton.addActionListener(new addDeleteAccountListener());

        deleteCustomerButton = new JButton("Del Customer");
        deleteCustomerButton.addActionListener(new addDeleteCustomerListener());

        // add all of them into bottom's west panel
        bwPanel.add(accountButton);
        bwPanel.add(customerButton);
        bwPanel.add(deleteAccountButton);
        bwPanel.add(deleteCustomerButton);

        /*
        Create a series of buttons
         */
        changePasswordButton = new JButton("ChangePsd");
        changePasswordButton.addActionListener(new changePsdListener());

        saveAccountButton = new JButton("Save Account");
        saveAccountButton.addActionListener(new saveAccountListener());

        exitButton = new JButton("Exit");

        // add an action listener to the exit button
        // using the lambda
        exitButton.addActionListener(e -> {
            // when exit save the message to file
            customers = Customer.getCustomersList();
            Customer.saveCustomer(customers);

            // when exit save the message to file
            for (Customer customer: customers)
                Account.saveAccount(customers, customer.getName());
            System.exit(-1);
        });

        // add all of them to bottom's center panel
        bcPanel.add(changePasswordButton);

        // set a size between the button
        bcPanel.add(new Label(""));
        bcPanel.add(saveAccountButton);

        // set a size between the button
        bcPanel.add(new Label());
        bcPanel.add(exitButton);

        // add the panel to the west and center
        bottomPanel.add(bwPanel, BorderLayout.WEST);
        bottomPanel.add(bcPanel, BorderLayout.CENTER);

        // add title border
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Functions : "));

        // add the top, middle, bottom to the center panel
        centerPanel.add(topPanel);
        centerPanel.add(middlePanel);
        centerPanel.add(bottomPanel);

        // set the JTextArea rows and columns
        msgArea = new JTextArea(28, 24);

        // add the JScrollPane to the JTextArea
        msgPane = new JScrollPane(msgArea);

        // the message area the change the line automatic
        msgArea.setLineWrap(true);

        // the message area cannot unmodifiable
        msgArea.setEditable(false);

        // set the msgArea font
        msgArea.setFont(new Font("Arial",Font.BOLD,12));

        // only show the vertical scroll bar
        msgPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        msgPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // set the preferred size for the east panel
        eastPanel.setPreferredSize(new Dimension(300, 350));
        eastPanel.add(msgPane);

        // set the border
        eastPanel.setBorder(BorderFactory.createTitledBorder("Operation info : "));

        // using the system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // add all the panel to the frame
        serverUI.add(westPanel, BorderLayout.WEST);
        serverUI.add(centerPanel, BorderLayout.CENTER);
        serverUI.add(eastPanel, BorderLayout.EAST);

        // add the window listener
        serverUI.addWindowListener(new windowListener());
        serverUI.setTitle("Bank Server");
        serverUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        serverUI.setVisible(true);
        serverUI.pack();
    }

    /**
     * Get the message area
     *
     * @return the JTextArea variable
     */
    public JTextArea getMsgArea() {
        return msgArea;
    }

    /**
     * Get the save account button
     *
     * @return the JButton variable
     */
    public JButton getSaveAccountButton() {
        return saveAccountButton;
    }

    /**
     * Get the user name JList
     *
     * @return the JList variable
     */
    public JList<String> getList() {
        return list;
    }

    /**
     * Get the account type comboBox
     *
     * @return the JComboBox variable
     */
    public JComboBox<String> getComboBox() {
        return comboBox;
    }

    /**
     * Get the balance field
     *
     * @return the JTextField variable
     */
    public JTextField getBalanceField() {
        return balanceField;
    }

    /**
     * Get the overdraft field
     *
     * @return the JTextField variable
     */
    public JTextField getOverDraftField() {
        return overDraftField;
    }

    /**
     * Get the interest field
     *
     * @return the JTextField variable
     */
    public JTextField getInterestField() {
        return interestField;
    }

    /**
     * The add account button listener
     * Once click the Add Account button, it will jump out
     * of a pop-up and ask who you want to add account
     */
    class addAccountListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // get the client name and store them in the vector
            Vector<String> temp = new Vector<>(customers.size());
            for (Customer customer : customers)
                temp.add(customer.getName());
            temp.sort(String::compareTo);

            // get the client name that need to add account
            String name = (String) JOptionPane.showInputDialog(serverUI, "Please select the client name!\n",
                    "Add Account", JOptionPane.QUESTION_MESSAGE, null, temp.toArray(), null);

            // if don't select a name it will do nothing
            if (name != null) {
                for (Customer customer : customers) {
                    // find the client in the LinkedList
                    if (customer.getName().equals(name)) {
                        // if the client account not empty
                        // it will create the account
                        if (customer.getAccounts() == null) {
                            // create the account list
                            customer.setAccounts(new LinkedList<>());
                            // create the account
                            customer.addAccount(new CheckingAccount());
                            customer.addAccount(new SavingsAccount());
                            // save the account in the file
                            Account.saveAccount(customers, name);
                            messageArea.setText("You add the account to the " + name);

                        }
                        // if the client already has the account if will show a message
                        else if (customer.getNumOfAccounts() == 2) {
                            JOptionPane.showMessageDialog(serverUI, "The client " + name + " already have account",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                            messageArea.setText("The client " + name + " already have account");
                        } else {
                            // create the account
                            customer.addAccount(new CheckingAccount());
                            customer.addAccount(new SavingsAccount());
                            // save the account in the file
                            Account.saveAccount(customers, name);
                            messageArea.setText("You add the account to the " + name);
                        }
                    }
                }
            }
        }
    }

    /**
     * The add customer button listener
     * Once you click the Add Customer button, it will jump out
     * of a pop-up and need you typing the client name that need to add
     */
    class addCustomerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // show a dialog and need the user input the client name
            String name = JOptionPane.showInputDialog(serverUI, "Please type the client name ",
                    "Add Customer", JOptionPane.INFORMATION_MESSAGE);

            // if don't click cancel
            if (name != null) {
                // if the client name not the empty
                if (name.equals("")) {
                    JOptionPane.showMessageDialog(serverUI, "Wrong type name", "Warning",
                            JOptionPane.WARNING_MESSAGE);
                    messageArea.setText("Wrong type name.");
                } else {
                    // add the new client in the list and set the initial password
                    customers.add(new Customer(name, "000000"));

                    Account.saveAccount(customers, name);

                    // show add the new client success
                    JOptionPane.showMessageDialog(serverUI, "New client " + name + "'s password is 000000.\n" +
                            "Please change password soon!", "Change psd", JOptionPane.WARNING_MESSAGE);

                    messageArea.setText("New client " + name + "'s password is 000000.\n" +
                            "Please change password soon!");
                }
            }
            // save the data to the file
            Customer.saveCustomer(customers);


            // renew the data in the JList
            Vector<String> temp = new Vector<>(customers.size());
            for (Customer customer : customers)
                temp.add(customer.getName());
            temp.sort(String::compareTo);
            // show the new data in the JList
            list.setListData(temp);

        }
    }

    /**
     * The delete button listener
     * Once you click the Delete Account button, it will jump out
     * of a pop-up and ask which one's account you need ta delete
     */
    class addDeleteAccountListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // get the client list
            Vector<String> temp = new Vector<>(customers.size());
            for (Customer customer : customers)
                temp.add(customer.getName());
            temp.sort(String::compareTo);

            // get the client name that delete account
            String name = (String) JOptionPane.showInputDialog(serverUI, "Please select the client name!\n",
                    "Delete Account", JOptionPane.QUESTION_MESSAGE, null, temp.toArray(), null);

            // delete the client account
            // and save to the file
            for (Customer customer : customers) {
                if (customer.getName().equals(name)) {
                    customer.setAccounts(null);
                    messageArea.setText("You have delete the " + name + "'s account.");

                    // save to the file
                    Account.saveAccount(customers, name);

                    // delete the account
                    DeleteFile.deleteAccount(customer.getName());
                    DeleteFile.deleteAccount(customer.getName());
                }
            }
        }
    }

    /**
     * The delete customer button listener
     * Once you click the Delete Customer button, it will jump out
     * of a pop-up and ask who you want to delete
     */
    class addDeleteCustomerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // get the client list
            Vector<String> temp = new Vector<>(customers.size());
            for (Customer customer : customers)
                temp.add(customer.getName());
            temp.sort(String::compareTo);

            // get the client name that need to delete
            String name = (String) JOptionPane.showInputDialog(serverUI, "Please enter the client name" +
                            " you want to delete", "Delete Customer", JOptionPane.WARNING_MESSAGE, null,
                    temp.toArray(), null);

            // delete it from the customer list
            for (int i = 0; i < customers.size(); i++) {
                if (customers.get(i).getName().equals(name)) {
                    messageArea.setText("You delete a customer : " + name);

                    // delete the customer
                    DeleteFile.deleteCustomer(customers.get(i).getName());
                    DeleteFile.deleteCustomer(customers.get(i).getName());

                    // delete the customer in the list
                    customers.remove(i);
                    break;
                }
            }

            // renew the JList
            temp = new Vector<>(customers.size());
            for (Customer customer : customers)
                temp.add(customer.getName());
            temp.sort(String::compareTo);
            list.setListData(temp);

            // save the customer data to the file
            Customer.saveCustomer(customers);

        }
    }

    /**
     * The change password listener
     * Once you click it, it will jump out of a pop-up
     * and ask which one password you need to change
     */
    class changePsdListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //get the client list
            Vector<String> temp = new Vector<>(customers.size());
            for (Customer customer : customers)
                temp.add(customer.getName());
            temp.sort(String::compareTo);

            // get the client name that need to change the password
            String name = (String) JOptionPane.showInputDialog(serverUI, "Please enter the client's password" +
                            " you want to change", "Change password", JOptionPane.WARNING_MESSAGE, null,
                    temp.toArray(), null);

            // if don't click the cancel button
            if (name != null) {
                double tempNum;
                // only number allow
                while (true) {
                    try {
                        // get the password
                        String psd = JOptionPane.showInputDialog(serverUI, "Please enter the new password",
                                "Change password", JOptionPane.WARNING_MESSAGE);
                        tempNum = Double.parseDouble(psd);
                        break;
                    } catch (NumberFormatException e1) {
                        messageArea.setText("Wrong password type");
                    }
                }
                // show the changed password
                messageArea.setText(name + "'s password change to " + (int) tempNum + "\n");

                // change the password in customer list
                for (Customer customer : customers) {
                    if (customer.getName().equals(name)) {
                        customer.setPassword(String.valueOf((int) tempNum));
                    }
                }

                // save the data to the file
                Customer.saveCustomer(customers);
            }
        }
    }

    /**
     * The save account button listener
     * Once you click the Save Account button, it will jump out of a pop-up
     * and ask which account you need save
     */
    class saveAccountListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            // get the client name from the JList
            String clientName = list.getSelectedValue();

            // get the customer from the customer list
            for (Customer customer : customers) {
                if (customer.getName().equals(clientName))
                // if the customer account is empty it don't need to save
                if (customer.getAccounts() == null) {
                    // show the error message
                    JOptionPane.showMessageDialog(serverUI, "This client don't have" +
                            " any account", "Error", JOptionPane.ERROR_MESSAGE);
                    messageArea.setText("This client don't have any account");
                    Account.saveAccount(customers, clientName);
                    return;
                } else if (customer.getNumOfAccounts() == 0) {
                    // show the error message
                    JOptionPane.showMessageDialog(serverUI, "This client don't have" +
                            " any account", "Error", JOptionPane.ERROR_MESSAGE);
                    messageArea.setText("This client don't have any account");
                    Account.saveAccount(customers, clientName);
                    return;
                }
            }

//            String showMsg = "Are you sure save the " + clientName + "'s " + accountType + " account?\n" +
//                    "With the changed value:\nBalance: " + balanceField.getText() +
//                    "\nOverDraft: " + overDraftField.getText() +
//                    "\nInterest: " + interestField.getText();
//
//            // 0 for yes, and 1 for no
//            int n = JOptionPane.showConfirmDialog(serverUI, showMsg, "Save Account", JOptionPane.YES_NO_OPTION);
//
//            if (n == 0) {
//                for (Customer customer : customers) {
//                    if (customer.getName().equals(clientName)) {
//                        if (accountType.equals("Checking")) {
//                            ((CheckingAccount) customer.getAccount(0))
//                                    .setBalance(Double.parseDouble(balanceField.getText()));
//                            ((CheckingAccount) customer.getAccount(0))
//                                    .setOverdraftProtection(Double.parseDouble(overDraftField.getText()));
//                        } else {
//                            ((SavingsAccount) customer.getAccount(1))
//                                    .setBalance(Double.parseDouble(balanceField.getText()));
//                            ((SavingsAccount) customer.getAccount(1))
//                                    .setInterestRate(Double.parseDouble(interestField.getText()));
//                        }
//                    }
//                }
//                Account.saveAccount(customers, clientName);
//            }
        }
    }

    /**
     * The comboBox listener
     * Once you change account type in the comboBox,
     * it will change in the field and show it in the message area
     */
    class comboBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // get the user name from the list
            String userName = list.getSelectedValue();
            // get the account type form the comboBox
            String accountType = comboBox.getItemAt(comboBox.getSelectedIndex());

            // get the accountList using the user name
            accounts = Customer.loadAccountMsg(userName);

            if (userName == null) {
                // if the client's account is empty it show nothing
                messageArea.setText(accountType + " account : null");
                balanceField.setText("null");
                overDraftField.setText("null");
                interestField.setText("null");
                balanceField.setEditable(false);
                overDraftField.setEditable(false);
                interestField.setEditable(false);
            } else {
                if (accounts.size() == 0) {
                    // if the client's account is empty it show nothing
                    messageArea.setText(userName + " don't have any account!");
                    balanceField.setText("null");
                    overDraftField.setText("null");
                    interestField.setText("null");
                    balanceField.setEditable(false);
                    overDraftField.setEditable(false);
                    interestField.setEditable(false);
                } // if the client account is not empty
                else if (accountType.equals("Checking")) {
                    // if the account type is 'Checking' it show the check account message

                    // get the 'Checking Account' account message
                    CheckingAccount checking = (CheckingAccount) accounts.get(0);

                    // show the 'Checking Account' message in the message area
                    messageArea.setText(accountType + " account : \nBalance:" + checking.getBalance()
                            + "\nOverDraft: " + checking.getOverdraftProtection());

                    // show the 'Checking Account' message in the field
                    balanceField.setText(String.valueOf(checking.getBalance()));
                    overDraftField.setText(String.valueOf(checking.getOverdraftProtection()));

                    // the 'Checking Account' don't have the interest
                    interestField.setText("null");

                    // set the state of the field
                    balanceField.setEditable(true);
                    overDraftField.setEditable(true);
                    interestField.setEditable(false);
                } else {
                    // if the account type is 'Saving' it show the saving account message

                    // get the 'Saving Account' account message
                    SavingsAccount saving = (SavingsAccount) accounts.get(1);

                    // show the 'Saving Account' message in the message area
                    messageArea.setText(accountType + " account : \nBalance:" + saving.getBalance()
                            + "\nInterest: " + saving.getInterestRate());

                    // show the 'Saving Account' message in the field
                    balanceField.setText(String.valueOf(saving.getBalance()));
                    // the 'Saving Account' don't have the overdraft
                    overDraftField.setText("null");
                    interestField.setText(String.valueOf(saving.getInterestRate()));

                    // set the state of the field
                    balanceField.setEditable(true);
                    overDraftField.setEditable(false);
                    interestField.setEditable(true);
                }
            }
        }
    }

    /**
     * The list listener
     * Once you change the client name in the list and the account type is not null
     * it will change in the field and show it in the message area
     */
    class listListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            // if the select value is change
            if (e.getValueIsAdjusting()) {
                // get the user name from the list
                String userName = list.getSelectedValue();
                // get the account type from the comboBox
                String accountType = comboBox.getItemAt(comboBox.getSelectedIndex());

                // if the account type is null it will do nothing
                if (accountType != null && !accountType.equals("")) {
                    // if the user name if null it will do nothing
                    if (userName == null) {

                    } else {
                        // get the account message with the client name
                        accounts = Customer.loadAccountMsg(userName);

                        if (accounts.size() == 0) {
                            // if the client's account is empty, it show nothing
                            messageArea.setText(userName + " don't have any account!");
                            balanceField.setText("null");
                            overDraftField.setText("null");
                            interestField.setText("null");
                            balanceField.setEditable(false);
                            overDraftField.setEditable(false);
                            interestField.setEditable(false);
                        } else if (accountType.equals("Checking")) {
                            // if the account type is 'Checking' it show the check account message

                            // get the 'Checking Account' account message
                            CheckingAccount checking = (CheckingAccount) accounts.get(0);

                            // show the 'Checking Account' message in the message area
                            messageArea.setText(accountType + " account : \nBalance:" + checking.getBalance()
                                    + "\nOverDraft: " + checking.getOverdraftProtection());

                            // show the 'Checking Account' message in the field
                            balanceField.setText(String.valueOf(checking.getBalance()));
                            overDraftField.setText(String.valueOf(checking.getOverdraftProtection()));

                            // the 'Checking Account' don't have the interest
                            interestField.setText("null");

                            // set the state of the field
                            balanceField.setEditable(true);
                            overDraftField.setEditable(true);
                            interestField.setEditable(false);
                        } else if (accountType.equals("Saving")) {
                            // if the account type is 'Saving' it show the saving account message

                            // get the 'Saving Account' account message
                            SavingsAccount saving = (SavingsAccount) accounts.get(1);

                            // show the 'Saving Account' message in the message area
                            messageArea.setText(accountType + " account : \nBalance:" + saving.getBalance()
                                    + "\nInterest: " + saving.getInterestRate());

                            // show the 'Saving Account' message in the field
                            balanceField.setText(String.valueOf(saving.getBalance()));
                            // the 'Saving Account' don't have the overdraft
                            overDraftField.setText("null");
                            interestField.setText(String.valueOf(saving.getInterestRate()));

                            // set the state of the field
                            balanceField.setEditable(true);
                            overDraftField.setEditable(false);
                            interestField.setEditable(true);
                        }
                    }
                }
            }
        }
    }

    /**
     * The window listener
     */
    class windowListener extends WindowAdapter {
        @Override
        public void windowClosed(WindowEvent e) {
            // if the window close, it close the thread
            customers = Customer.getCustomersList();
            Customer.saveCustomer(customers);
            for (Customer customer: customers)
                Account.saveAccount(customers, customer.getName());

            System.exit(-1);
        }
    }

    /**
     * The keyboard listener
     * Don't allow type the letters
     */
    class voteElectyKeyListener extends KeyAdapter {
        @Override
        public void keyTyped(KeyEvent e) {
            // get the typing char
            int keyChar = e.getKeyChar();
            // if the char is from '1' to '9' and '.' , it do nothing
            if (keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9 || keyChar == KeyEvent.VK_PERIOD) {

            } else {
                // if not, not allow to type
                e.consume();
            }
            // if typing length large than 10, stop typing also
            String s = balanceField.getText();
            if (s.length() >= 10) e.consume();
        }
    }

//    class changePsdFrame {
//        public String PsdFrame() {
//            JFrame frame = new JFrame();
//            JPanel panel = new JPanel();
//            JLabel label = new JLabel();
//            JComboBox<String> nameList;
//
//            Vector<String> temp = new Vector<>(customers.size());
//            for (Customer customer : customers)
//                temp.add(customer.getName());
//            temp.sort(String::compareTo);
//
//            nameList = new JComboBox<>(temp);
//            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//            label.setText("Please enter the client's password you want to change");
//
//            panel.add(label);
//            panel.add(nameList);
//
//            frame.add(panel);
//            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//            frame.setTitle("Change password");
//            frame.setVisible(true);
//        }
//    }
}
