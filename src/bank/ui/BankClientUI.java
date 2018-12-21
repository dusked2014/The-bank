/*
 * Copyright (c) 18-11-19 下午4:08
 * 李高丞 @版权所有人
 * Version 1.0 Beta
 */

package bank.ui;


import bank.domain.Account;
import bank.domain.CheckingAccount;
import bank.domain.OverdraftException;
import bank.domain.SavingsAccount;
import bank.net.Connect;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * The 'BankClientUI' class, which designs the interface of the bank
 * client, defines a series of listeners, but there are still some
 * hidden problems unsolved
 *
 * @author 李高丞
 * @version 1.0 Beta
 */
public class BankClientUI extends JFrame {
    // where the client files are saved
    private static String filePath = "C:\\Users\\hasee\\Documents\\JavaCode\\The Banking";

    // create the BankClientUI and using the singleton design patten
    private static BankClientUI clientUI = new BankClientUI();

    // create the account type list
    private static List<String> msgList;

    // create the client account list
    private static LinkedList<Account> clientAccount;

    // create the center panel
    private JPanel centerPanel;

    // create the east panel
    private JPanel eastPanel;

    // create the money field which typing the money in there
    private JTextField moneyField;

    // create the deposit button
    private JButton depositButton;

    // create the withdraw button
    private JButton withDrawButton;

    // create the center area show the operation message
    private JTextArea centerArea;

    // the border
    private Border border;

    // create the east TopEast area which show the account message
    private JTextArea eTopEastArea;

    // create the account type list
    private JList<String> accountType;

    /**
     * Default constructor
     */
    private BankClientUI() {

    }

    /**
     * Get the 'BankClientUI' variable
     *
     * @return the BankClientUI variable
     */
    public static BankClientUI getClientUI() {
        return clientUI;
    }

    /**
     * A test to show the GUI
     *
     * @param args command line parameter
     */
    public static void main(String[] args) {
        BankClientUI clientUI = new BankClientUI();
        clientUI.lunchGUI();
        //loadToAccount("test");
    }

    /**
     * Start the 'BankClientUI' interface
     */
    public void lunchGUI() {
        // get the BankClientUI variable
        BankClientUI clientUI = getClientUI();

        // create the center panel
        centerPanel = new JPanel();

        // set the title border for the center panel
        border = BorderFactory.createTitledBorder("Operation info :");
        centerPanel.setBorder(border);

        // create the east panel
        eastPanel = new JPanel();

        // set the title border for the east panel
        border = BorderFactory.createTitledBorder("User Msg :");
        eastPanel.setBorder(border);

        // set up size and layout manager
        clientUI.setPreferredSize(new Dimension(500, 400));
        clientUI.setLayout(new BorderLayout());
        // set the BoxLayout to Y axis
        eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));

        // set the panel size
        eastPanel.setPreferredSize(new Dimension(250, 300));
        centerPanel.setPreferredSize(new Dimension(250, 300));

        // set the center area rows and columns
        centerArea = new JTextArea(20, 18);

        // set the center area change line automatic
        centerArea.setLineWrap(true);

        // set the center area unmodifiable
        centerArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(centerArea);

        // only show the vertical scrollbar
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        centerPanel.add(scrollPane);

        // create the panel for the east panel
        JPanel eTopPanel = new JPanel();
        JPanel eMiddlePanel = new JPanel();
        JPanel eBottomPanel = new JPanel();
        JPanel eMCenterPanel = new JPanel();
        JPanel eMEastPanel = new JPanel();

        // set the account type
        String[] type = {"Checking", "Saving"};
        accountType = new JList<>(type);

        // set the area size
        eTopEastArea = new JTextArea(5, 20);

        // add a list listener for the account
        accountType.addListSelectionListener(new listListener());

        // set the center area change line automatic
        eTopEastArea.setLineWrap(true);

        // set the center area unmodifiable
        eTopEastArea.setEditable(false);

        eTopPanel.add(accountType, BorderLayout.CENTER);
        eTopPanel.add(eTopEastArea, BorderLayout.EAST);

        // create a title border for the east top panel
        border = BorderFactory.createTitledBorder("Account & Msg");
        eTopPanel.setBorder(border);

        // set the money label
        JLabel money = new JLabel("￥");

        // set the money field size
        moneyField = new JTextField(5);

        // create the deposit and withdraw button
        depositButton = new JButton("Deposit");
        withDrawButton = new JButton("WithDraw");

        eMCenterPanel.add(money);
        eMCenterPanel.add(moneyField);

        // add the key listener and action listener to the field and button
        moneyField.addKeyListener(new voteElectyKeyListener());
        depositButton.addActionListener(new depositButtonListener());
        withDrawButton.addActionListener(new withDrawButtonListener());

        eMEastPanel.add(depositButton);
        eMEastPanel.add(withDrawButton);

        eMiddlePanel.add(eMCenterPanel);
        eMiddlePanel.add(eMEastPanel);

        // set the etched border for the east middle panel
        border = BorderFactory.createEtchedBorder();
        eMiddlePanel.setBorder(border);

        JButton exitButton = new JButton("Exit");

        // add the exit listener to the exit button
        exitButton.addActionListener(e -> System.exit(-1));

        eBottomPanel.add(exitButton);

        // set the etched border for the east bottom panel
        border = BorderFactory.createEtchedBorder();
        eBottomPanel.setBorder(border);


        eastPanel.add(eTopPanel);
        eastPanel.add(eMiddlePanel);
        eastPanel.add(eBottomPanel);

        // using the system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // add all the panel to the frame
        clientUI.add(eastPanel, BorderLayout.EAST);
        clientUI.add(centerPanel, BorderLayout.CENTER);

        // add the window listener
        clientUI.addWindowListener(new windowListener());
        clientUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        clientUI.setVisible(true);
        clientUI.pack();
    }

    /**
     * Get the center area
     *
     * @return the JTextArea variable
     */
    public JTextArea getCenterArea() {
        return centerArea;
    }

    /**
     * Get the top east area
     *
     * @return the JTextArea variable
     */
    public JTextArea geteTopEastArea() {
        return eTopEastArea;
    }

    /**
     * Get the money field
     *
     * @return the JTextField variable
     */
    public JTextField getMoneyField() {
        return moneyField;
    }

    /**
     * Get the deposit button
     *
     * @return the JButton variable
     */
    public JButton getDepositButton() {
        return depositButton;
    }

    /**
     * Get the withdraw button
     *
     * @return the JButton variable
     */
    public JButton getWithDrawButton() {
        return withDrawButton;
    }

    /**
     * The deposit button listener
     * One click the Deposit button, it will get the money in
     * the money field, and send a special request to the client
     */
    class depositButtonListener implements ActionListener {
        // get the account message from the JList
        JList<String> accountList = accountType;

        @Override
        public void actionPerformed(ActionEvent e) {

            // get the client name
            // only use in success connect
            String clientName = Connect.getConnect().getNameField().getText();

            // get the account type
            String accountType = accountList.getSelectedValue();

            // get the money from the money field
            double money = Double.parseDouble(moneyField.getText());

            // get the client message and store it into LinkedList
            // with using client name
            clientAccount = loadToAccount(clientName);
            msgList = loadToList(clientName);

            if (accountType.equals("Checking")) {
                // if the account type is the 'Checking'

                // get the 'Checking Account' account message
                CheckingAccount checkingAccount = (CheckingAccount) clientAccount.get(0);

                // show the 'Checking Account' message in the center area
                centerArea.append("Deposit Checking account money : " + money + "\n");

                // save the money
                checkingAccount.deposit(money);

                //saveAccount(clientAccount);

                // renew the message in the eTopEastArea
                eTopEastArea.setText(accountType + " account :\nBalance: " + checkingAccount.getBalance()
                        + "\nOverDraft: " + checkingAccount.getOverdraftProtection());

                // send the money changed message to the server
                Connect.moneyChange(accountType, "Deposit", String.valueOf(money));

            } else {
                // if the account type is the 'Saving'

                // get the 'Saving Account' account message
                SavingsAccount savingsAccount = (SavingsAccount) clientAccount.get(1);

                // show the 'Saving Account' message in the center area
                centerArea.append("Deposit Saving account money : " + money + "\n");

                // save the money
                savingsAccount.deposit(money);

                //saveAccount(clientAccount);


                // renew the message in the eTopEastArea
                eTopEastArea.setText(accountType + " account :\nBalance: " + savingsAccount.getBalance()
                        + "\nInterest: " + savingsAccount.getInterestRate());

                // send the money changed message to the server
                Connect.moneyChange(accountType, "Deposit", String.valueOf(money));
            }
        }
    }

    /**
     * The withDraw button listener
     * Once you click the WithDraw button, it will get the money in
     * the money field, and send a special request to the client
     */
    class withDrawButtonListener implements ActionListener {
        // get the account message from the JList
        JList<String> accountList = accountType;

        @Override
        public void actionPerformed(ActionEvent e) {

            // get the client name
            // only use in success connect
            String clientName = Connect.getConnect().getNameField().getText();

            // get the account type
            String accountType = accountList.getSelectedValue();

            // get the money from the money field
            double money = Double.parseDouble(moneyField.getText());

            // get the client message and store it into LinkedList
            // with using client name
            clientAccount = loadToAccount(clientName);

            try {
                if (accountType.equals("Checking")) {
                    // if the account type is the 'Checking'

                    // get the 'Checking Account' account message
                    CheckingAccount checkingAccount = (CheckingAccount) clientAccount.get(0);

                    // withdraw the money
                    checkingAccount.withdraw(money);

                    // show the 'Checking Account' message in the center area
                    centerArea.append("WithDraw Checking account money : " + money + "\n");

                    //saveAccount(clientAccount);

                    // renew the message in the eTopEastArea
                    eTopEastArea.setText(accountType + " account :\nBalance: " + checkingAccount.getBalance()
                            + "\nOverDraft: " + checkingAccount.getOverdraftProtection());

                    // send the money changed message to the server
                    Connect.moneyChange(accountType, "WithDraw", String.valueOf(money));

                } else {
                    // if the account type is the 'Saving'

                    // get the 'Saving Account' account message
                    SavingsAccount savingsAccount = (SavingsAccount) clientAccount.get(1);

                    // withdraw the money
                    savingsAccount.withdraw(money);

                    // show the 'Saving Account' message in the center area
                    centerArea.append("WithDraw Saving account money : " + money + "\n");

                    //saveAccount(clientAccount);

                    // renew the message in the eTopEastArea
                    eTopEastArea.setText(accountType + " account :\nBalance: " + savingsAccount.getBalance()
                            + "\nInterest: " + savingsAccount.getInterestRate());

                    // send the money changed message to the server
                    Connect.moneyChange(accountType, "WithDraw", String.valueOf(money));

                }
            } catch (OverdraftException e1) {
                // catch the OverdraftException

                // show the error message in the center area
                centerArea.append("[ERROR]: You can withdraw money anymore, " +
                        "you don't have enougn money, " + "you need " +
                        e1.getDeficit() + "￥ money!\n");

                //e1.getCause();
                //e1.printStackTrace();
            }
        }
    }

    /**
     * The JList listener
     * Once you change the value in the list
     * it will show the change message in the eTopEastArea
     */
    class listListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {

            // get the client name
            // only use in success connect
            String clientName = Connect.getConnect().getNameField().getText();

            // get the account type from the list
            String type = accountType.getSelectedValue();

            // load the account message using the client name
            msgList = loadToList(clientName);

            // if the account is empty
            if (msgList.size() == 0) {

                // show no account in the eTopEastArea
                eTopEastArea.setText("No any account.");
                return;
            }
            if (type.equals("Checking")) {
                // show the 'Checking Account' message in the eTopEastArea
                eTopEastArea.setText(type + " account :\nBalance: " + msgList.get(0)
                        + "\nOverDraft: " + msgList.get(1));
            } else {
                // show the 'Saving Account' message in the eTopEastArea
                eTopEastArea.setText(type + " account :\nBalance: " + msgList.get(2)
                        + "\nInterest: " + msgList.get(3));
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
            System.exit(-1);
        }

    }

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
        }
    }

    /**
     * This static method 'loadToList' which can get the account file
     * with using the client name, and store the account message in the List
     *
     * @param clientName the client that need to searche
     * @return the client message list
     */
    public static List<String> loadToList(String clientName) {

        // the file path where the client account message saved
        String filePath = "C:\\Users\\hasee\\Documents\\JavaCode\\" +
                "The Banking\\" + clientName + "\\" + "Account.txt";

        // find the account file
        File file = new File(filePath);

        // create the reader
        BufferedReader br;

        // new account message list
        msgList = new LinkedList<>();

        try {
            // using the reader read the account file
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            String str;

            while ((str = br.readLine()) != null) {
                // split with the a space
                String[] strs = str.split(" ");

                if (strs.length < 4) {
                    System.out.println("Wrong type: " + str);
                    return msgList;
                }

                // add the account message to the account message list
                msgList.add(strs[1]);
                msgList.add(strs[3]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return msgList;
    }

    /**
     * In this static method, it store the account message to the file, with using the
     * LinkedList
     *
     * @param accounts the LinkedList the need to be store account
     */
    public static void saveAccount(LinkedList<Account> accounts) {

        // get the client name
        // only use in success connect
        String clientName = Connect.getConnect().getNameField().getText();
        File file;

        // the stream that save the file
        OutputStreamWriter osw = null;
        FileOutputStream fos;

        try {
            // where the file saved
            file = new File(filePath + "\\" + clientName + "\\Account.txt");

            // initialize the save file stream
            fos = new FileOutputStream(file);
            osw = new OutputStreamWriter(fos);

            int type = 0; // judge the save account type
            String str = "";
            for (Account account : accounts) {
                // if the type is 0, the saved account is 'Checking Account'
                // if the type is 1, the saved account is 'Saving Account'

                if (type == 0) {
                    // the save message
                    str += "Balance " + account.getBalance() +
                            " OverDraft " + ((CheckingAccount) account).getOverdraftProtection()
                            + "\n";
                }
                if (type == 1) {
                    // the save message
                    str += "Balance " + account.getBalance() +
                            " Interest " + ((SavingsAccount) account).getInterestRate()
                            + "\n";
                }
                type++;
            }

            // writr the save message to the file
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

    /**
     * In this static method, it need the client name,
     * then it return the linked list which store the account
     * message
     *
     * @param clientName the client name that need to search
     * @return the linked list which store the account message
     */
    public static LinkedList<Account> loadToAccount(String clientName) {

        // fine the client account file
        File clientFile = new File(filePath + "\\" + clientName + "\\Account.txt");

        // the reader read the client account file
        BufferedReader br;

        // new client account message list
        clientAccount = new LinkedList<>();

        try {
            // create the read file stream
            br = new BufferedReader(new InputStreamReader(new FileInputStream(clientFile)));

            String str;

            int type = 0; // judge the save account type

            while ((str = br.readLine()) != null) {

                // split the account message with a space
                String[] strs = str.split(" ");

                if (strs.length < 4)
                    System.out.println("Wrong type: " + str);

                // if the type is 0, the saved account is 'Checking Account'
                // if the type is 1, the saved account is 'Saving Account'
                if (type == 0) {
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
            e.printStackTrace();
        }
//        System.out.println(clientAccount.get(0).getBalance());
//        System.out.println(clientAccount.get(1).getBalance());
        return clientAccount;
    }
}
