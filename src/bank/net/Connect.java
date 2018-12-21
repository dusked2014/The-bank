/*
 * Copyright (c) 18-11-19 下午4:08
 * 李高丞 @版权所有人
 * Version 1.0 Beta
 */

package bank.net;

import bank.domain.*;
import bank.ui.BankClientUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.Socket;
import java.util.LinkedList;


/**
 * In the connect class, it has the login frame, which can connect to the server
 * it call the BankClientUI to operate withdraw and deposit the balance in the account
 *
 * @author 李高丞
 * @version 1.0 Beta
 */
public class Connect {
    // the input stream that the server put
    private BufferedReader serverIn;

    // the client socket
    static Socket connection;

    // get the BankClientUI
    private static BankClientUI clientUI = BankClientUI.getClientUI();

    // the login frame
    private JFrame frame;
    private JPanel mainPanel;

    // create the connect variable
    private static Connect connect = new Connect();

    // the client output stream
    private static PrintStream ps;

    // the login frame container
    private static JPasswordField psdField;
    private static JTextField nameField;
    private static LinkedList<Account> accounts;


    /**
     * A test method
     *
     * @param args command line parameter
     */
    public static void main(String[] args) {
        Connect connect = Connect.getConnect();
        connect.lunchClient();
    }

    /**
     * Get the connect
     *
     * @return the Connect variable
     */
    public static Connect getConnect() {
        return connect;
    }

    /**
     * Default constructor
     */
    private Connect() {

    }

    /**
     * lunch the client
     */
    public void lunchClient() {
        setUpGUI();
        setUpConnect();
    }

    /**
     * lunch the client connect
     */
    public void setUpConnect() {
        try {

            // a read thread which continous read output stream from the server
            Thread thread = new Thread(new RemoteReader());

            // connect the server
            connection = new Socket("127.0.0.1", 5432);

            // keep the connect alive
            connection.setKeepAlive(true);

            // get the output stream and input stream
            ps = new PrintStream(connection.getOutputStream());
            serverIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            thread.start();

        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.err.println("Can not connect the server.");
        }
    }

    /**
     * lunch the client GUI
     */
    public void setUpGUI() {

        // the login frame
        frame = new JFrame();
        mainPanel = new JPanel();
        JPanel topPanel = new JPanel();
        JPanel middlePanel = new JPanel();
        JPanel bottomPanel = new JPanel();

        // the name label
        JLabel nameLabel = new JLabel("Name: ");

        // set the label length
        nameField = new JTextField(10);

        // add an action listener
        nameField.addActionListener(new confirmActionListener());

        topPanel.add(nameLabel);
        topPanel.add(nameField);

        // the password label
        JLabel psdLabel = new JLabel("Password: ");

        // set the password field length
        psdField = new JPasswordField(12);

        // add the action listener and key listener to the password field
        psdField.addActionListener(new confirmActionListener());
        psdField.addKeyListener(new voteElectyKeyListener());

        // nothing
        psdField.setEchoChar('♥');

        middlePanel.add(psdLabel);
        middlePanel.add(psdField);

        // two button
        JButton confirmButton = new JButton("Confirm");
        JButton cancelButton = new JButton("Cancel");

        // add the confirm button listener
        confirmButton.addActionListener(new confirmActionListener());

        // add the cancel button listener
        cancelButton.addActionListener(e -> System.exit(-1));

        bottomPanel.add(confirmButton);
        bottomPanel.add(cancelButton);

        // set the login frame layout manager
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // set the login frame size
        mainPanel.setPreferredSize(new Dimension(250, 130));
        mainPanel.add(topPanel);
        mainPanel.add(middlePanel);
        mainPanel.add(bottomPanel);

//        // using the system look and feel
//        try {
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        frame.add(mainPanel);
        frame.setTitle("Login");
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Keep receive message from the server
     */
    private class RemoteReader implements Runnable {
        @Override
        public void run() {
            String Msg;

            try {
                while (true) {
                    // receive message
                    if ((Msg = serverIn.readLine()) != null) {

                        // "#ACS" means account change successfully
                        // click the deposit or withdraw button, if the server change success,
                        // it will send this message
                        if (Msg.contains("#ACS#")) {

                            // get the client account message
                            accounts = BankClientUI.loadToAccount(nameField.getText());

                            // split the Msg with space
                            String[] strs = Msg.split(" ");
                            if (strs.length < 7) return;

                            try {
                                // save the change
                                if (strs[2].equals("Checking")) {
                                    CheckingAccount account = (CheckingAccount) accounts.get(0);

                                    if (strs[4].equals("WithDraw")) {
                                        account.withdraw(Double.parseDouble(strs[6]));
                                    } else if (strs[4].equals("Deposit")) {
                                        account.deposit(Double.parseDouble(strs[6]));
                                    }

                                } else if (strs[2].equals("Saving")) {
                                    SavingsAccount account = (SavingsAccount) accounts.get(1);

                                    if (strs[4].equals("WithDraw")) {
                                        account.withdraw(Double.parseDouble(strs[6]));
                                    } else if (strs[4].equals("Deposit")) {
                                        account.deposit(Double.parseDouble(strs[6]));
                                    }
                                }

                                // save the change to the file
                                BankClientUI.saveAccount(accounts);

                            } catch (OverdraftException e) {
                                // if withdraw too much money, it will throw this exception

                                clientUI.getCenterArea().append("You cannot withdraw money now!\n" +
                                        "You need " + e.getDeficit() + "￥ money.");
                            }
                        }

                        // other type of message
                        switch (Msg) {
                            case "#true":
                                // "#true" means login success

                                BankClientUI clientUI = BankClientUI.getClientUI();

                                // start read file from the server
                                ReadFile readFile = new ReadFile();

                                clientUI.setTitle("Welcome --- " + nameField.getText());

                                // lunch the client GUI
                                clientUI.lunchGUI();

                                // set the Login frame can not be see
                                frame.setVisible(false);
                                readFile.receiveFile(connection);
                                break;
                            case "#false":
                                // "#false" means login fail

                                // show to the client, ask to check the ID or password
                                JOptionPane.showMessageDialog(mainPanel, "Please check you name of password",
                                        "Wrong", JOptionPane.ERROR_MESSAGE);
                                break;
                            case "#change":
                                // "#change" means server change you account

                                readFile = new ReadFile();
                                // receive the changed file
                                readFile.receiveFile(connection);
                                break;
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Cannot connect the server!");
                System.exit(-1);
            }

        }
    }

    /**
     * Get the name field
     *
     * @return the JTextField variable
     */
    public JTextField getNameField() {
        return nameField;
    }

    /**
     * Get the password field
     *
     * @return the JPasswordField variable
     */
    public JPasswordField getPsdField() {
        return psdField;
    }

    /**
     * Send the changed money to the server
     *
     * @param accountType  the change account type
     * @param changedType  the changed type "Deposit" or "WithDraw"
     * @param changedMoney the changed money
     */
    public static void moneyChange(String accountType, String changedType, String changedMoney) {
        ps.println("#MoneyChange " + new String(psdField.getPassword()) +
                " AccountType " + accountType +
                " ChangeType " + changedType +
                " ChangeMoney " + changedMoney +
                " " + nameField.getText());
    }

//    public static void change(String accountType, String changedType, String changedMoney) {
//        String clientName = Connect.getConnect().getNameField().getText();
//        double money = Double.parseDouble(changedMoney);
//        accounts = BankClientUI.loadToAccount(clientName);
//        JTextArea area = clientUI.getCenterArea();
//        JTextArea etearea = clientUI.geteTopEastArea();
//
//
//        try {
//
//            if (accountType.equals("Checking")) {
//                CheckingAccount checkingAccount = (CheckingAccount) accounts.get(0);
//                checkingAccount.withdraw(money);
//                area.append("WithDraw Checking account money : " + money + "\n");
//
//                accountsList = BankClientUI.loadToList(clientName);
//                etearea.setText(accountType + " account :\nBalance: " + accountsList.get(0)
//                        + "\nOverDraft: " + accountsList.get(1));
//
//                Connect.moneyChange(accountType, "WithDraw", String.valueOf(money));
//
//            } else {
//                SavingsAccount savingsAccount = (SavingsAccount) accounts.get(1);
//                savingsAccount.withdraw(money);
//                area.append("WithDraw Saving account money : " + money + "\n");
//
//                accountsList = BankClientUI.loadToList(clientName);
//                etearea.setText(accountType + " account :\nBalance: " + accountsList.get(2)
//                        + "\nInterest: " + accountsList.get(3));
//
//                Connect.moneyChange(accountType, "WithDraw", String.valueOf(money));
//
//            }
//        } catch (OverdraftException e){
//            e.printStackTrace();
//        }
//    }

    /**
     * ConfirmButton Action Listener
     * Once click the confirm button, it send the ID and password to server
     * and wait for the server confirm the ID and password is match or not
     */
    class confirmActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String name = nameField.getText();
            String psd = new String(psdField.getPassword());

            // send the ID and password to the server and wait for the server
            ps.println("#Login " + name.replaceAll(" ", "") + " " + psd);
            System.out.println(name.replaceAll(" ", "") + " " + psd);

        }
    }

    /**
     * Read file from the server
     * if it get the file name, it start to receive file
     */
    class ReadFile {
        public void receiveFile(Socket s) throws IOException {

            // set the input stream
            DataInputStream dis = new DataInputStream(s.getInputStream());

            // read the file name
            String fileName = dis.readUTF();

            // fix a bug
            if (fileName.equals("#change")) {
                // "#change" means server change you account

                ReadFile readFile = new ReadFile();
                // receive the changed file
                readFile.receiveFile(connection);
                return;
            }

            // the save file path
            String filePath = new File("").getAbsolutePath() + "\\" + nameField.getText();

            //if (fileName.equals("#change")) System.out.println(fileName);

            // create the file
            File file = new File(filePath + "\\" + fileName);
            File file1 = new File(filePath);

            if (!file1.exists()) file1.mkdirs();
            if (!file.exists()) file.createNewFile();

            // get read to write the file
            FileOutputStream fos = new FileOutputStream(file);
            byte[] receData = new byte[1024];
            int length = 0;

            if (dis != null) length = dis.read(receData, 0, receData.length);

            // fix a bug
            if (new String(receData).equals("#change")) {
                // "#change" means server change you account

                ReadFile readFile = new ReadFile();
                // receive the changed file
                readFile.receiveFile(connection);
                return;
            }

            // write the file
            fos.write(receData, 0, length);
            fos.flush();
            fos.close();
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
            // if the char is from '1' to '9' , it do nothing
            if (keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9) {

            } else {
                // if not, not allow to type
                e.consume();
            }
        }

    }
}
