/*
 * Copyright (c) 18-11-19 下午4:08
 * 李高丞 @版权所有人
 * Version 1.0 Beta
 */

package bank.net;

import Data.CheckClient;
import Data.FindClient;
import bank.domain.Account;
import bank.domain.CheckingAccount;
import bank.domain.Customer;
import bank.domain.SavingsAccount;
import bank.ui.BankServerUI;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * The BankServer support support network connections from
 * CustomerUI to provide information and to receive information
 *
 * @author 李高丞
 * @version 1.0 Beta
 */
public class Server extends Thread {
    // the file save the client name and password
    static String filePath = "C:\\Users\\hasee\\Documents\\JavaCode\\The Banking\\ID&Psd.txt";

    // the file path where save the client account message
    static String accountPath = "C:\\Users\\hasee\\Documents\\JavaCode\\The Banking\\src\\Data\\Client\\";

    // get the BankServerUI variable
    private static BankServerUI serverUI = BankServerUI.getServerUI();

    // get the BankServerUI's operate JTextArea
    private JTextArea operateMsg = BankServerUI.getServerUI().getMsgArea();

    // the server socket
    private static ServerSocket serverSocket;

    // the client socket
    private Socket clientSocket;

    // a map save the client name and password
    private static Map<String, String> map = CheckClient.loadClientToMap(filePath);

    private static Map<Socket, String> messageMap = new HashMap<>();

    // a linked list save all the customers
    private static LinkedList<Customer> customers = Customer.getCustomersList();

    /**
     * The server constructor, every new client connnet the server,
     * if will print the IP address in the consolas
     *
     * @param clientSocket the client socket
     */
    public Server(Socket clientSocket) {
        this.clientSocket = clientSocket;
//        System.out.println("New connection: " + clientSocket.getInetAddress().getHostAddress() +
//                ":" + clientSocket.getPort() + " connect the server!");
        serverUI.getMsgArea().append("\nNew connection: " + clientSocket.getInetAddress().getHostAddress() +
                ":" + clientSocket.getPort() + " connect the server!\n");
    }

    /**
     * A test method
     *
     * @param args command line parameter
     * @throws IOException don't want to handle it
     */
    public static void main(String[] args) throws IOException {
        // initialize the server socket using port 5432
        serverSocket = new ServerSocket(5432);

        // lunch the BankServerUI
        BankServerUI serverUI = BankServerUI.getServerUI();
        serverUI.lunchGUI();

        // start the server thread, which can multiple CustomerUI applications
        new serverThread().start();

        // start the message thread, which can send the change message to the client
        new msgThread().start();
    }

    /**
     * Check the client ID and password
     *
     * @param outPut output stream print the message to the client
     * @param map    where store the client ID and password
     * @param name   the  client name which is Client send
     * @param psd    the client password which is Client send
     */
    private void checkIDAndPsd(PrintStream outPut, Map<String, String> map, String name, String psd) {
        int Boolean = 0; // judge the right or wrong

        // check the client ID and password
        for (String key : map.keySet()) {
            if (key.equals(name) && map.get(key).equals(psd))
                Boolean++;
        }

        // if Boolean is 1, it mean find the client in the map,
        // if Boolean is 0, it mean do not find the client in the map.
        if (Boolean == 1) {
            // print the correct message to the client
            outPut.println("#true");

            // show the message in the operate area
            operateMsg.append("\nSuccess connect!");

            // send the client account file to the client
            FileServer fileServer = new FileServer();
            fileServer.sendFileToClient(clientSocket, accountPath + name + "\\Account.txt");

        } else {
            // print the error message to the cilent
            outPut.println("#false");

            // show the message in the operate area
            operateMsg.append("\nConnect fail, wrong ID or psd.");
        }
    }


    public void run() {
        PrintStream ps;
        BufferedReader br;
        String Msg;

        try {
            ps = new PrintStream(clientSocket.getOutputStream());
            br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            while (true) {
                // read from the client socket ouput stream
                if ((Msg = br.readLine()) != null) {

                    if (Msg.contains("#Login")) { // if contain "#Login" it mean client try to login the server

                        // split the Msg with a space
                        String[] strs = Msg.split(" ");
                        if (strs.length != 3) {
                            ps.println("#false");
                            continue;
                        }

                        // show the request message
                        operateMsg.append("\nFrom: " + clientSocket.getInetAddress().getHostAddress()
                                + ":" + clientSocket.getPort() + " make a request.");
                        operateMsg.append("\nName: " + strs[1] + " psd:" + strs[2] + " try to connect.");

                        // load the client message
                        map = CheckClient.loadClientToMap(filePath);

                        // check the ID and password
                        checkIDAndPsd(ps, map, strs[1], strs[2]);

                        // add the client to the message map
                        messageMap.put(clientSocket, strs[1]);
                    }
                    if (Msg.contains("#MoneyChange")) {
                        // if contain "#MoneyChange" it means client want to change the money

                        // split Msg with a space
                        String[] strs = Msg.split(" ");
                        if (strs.length < 9) return;

                        String psd = strs[1];
                        String clientName = strs[8];
                        LinkedList<Account> accounts = new LinkedList<>();
                        customers = Customer.getCustomersList();

                        // find the client account message
                        for (Customer customer : customers) {
                            if (customer.getName().equals(clientName)) {
                                accounts = customer.getAccounts();
                                //System.out.println(0);
                            }
                        }

                        // save the change to the account list
                        if (strs[3].equals("Checking")) {
                            CheckingAccount account = (CheckingAccount) accounts.get(0);

                            if (strs[5].equals("Deposit"))
                                account.deposit(Double.parseDouble(strs[7]));
                            if (strs[5].equals("WithDraw"))
                                account.withdraw(Double.parseDouble(strs[7]));
                        } else if (strs[3].equals("Saving")) {
                            SavingsAccount account = (SavingsAccount) accounts.get(1);

                            if (strs[5].equals("Deposit"))
                                account.deposit(Double.parseDouble(strs[7]));
                            if (strs[5].equals("WithDraw"))
                                account.withdraw(Double.parseDouble(strs[7]));
                        }

                        // save the account message to the file
                        Account.saveAccount(customers, clientName);

                        // print the message to the client, show server change the money success
                        ps.println("#ACS# " +
                                "AccountType " + strs[3] +
                                " ChangeType " + strs[5] +
                                " ChangeMoney " + strs[7]);

                        // show the message in the operation area
                        operateMsg.append("\n[MESSAGE] : " + clientSocket.getInetAddress().getHostAddress()
                                + ":" + clientSocket.getPort() + " Change his account " +
                                "\nAccountType :" + strs[3] +
                                "\nChangeType :" + strs[5] +
                                "\nChangeMoney :" + strs[7]);
                    }
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();

            // if some error happen, show it to the operate area
            operateMsg.append("\n" + "[MESSAGE] : " + clientSocket.getInetAddress().getHostAddress()
                    + ":" + clientSocket.getPort() + " quit the server!");
        }
    }

    /**
     * The server thread, allow many clinet login the server
     */
    static class serverThread extends Thread {
        @Override
        public void run() {
            serverUI.getMsgArea().setText("Server started... wait");
            try {
                serverUI.getMsgArea().append("\nServer start success!");
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    new Server(clientSocket).start();
                }
            } catch (IOException e) {
                System.out.println("Some problem happen !");
                System.exit(1);
            }
        }
    }

    /**
     * send saved message to the client
     * if the client send message contain '#change'
     */
    static class msgThread extends Thread {
        @Override
        public void run() {
            // create a servial variables

            JList<String> list = serverUI.getList();
            JComboBox<String> comboBox = serverUI.getComboBox();
            JButton saveButton = serverUI.getSaveAccountButton();
            JTextField balanceField = serverUI.getBalanceField();
            JTextField overDraftField = serverUI.getOverDraftField();
            JTextField interestField = serverUI.getInterestField();

            // add an action listener to the save button
            // if the button click, it will send the changed file to client
            saveButton.addActionListener(e -> {
                LinkedList<Customer> customers = Customer.getCustomersList();
                // todo : new add account cannot success save
                String clientName = list.getSelectedValue();
                String accountType = comboBox.getItemAt(comboBox.getSelectedIndex());


                for (Customer customer : customers) {
                    //System.out.println(customer.getNumOfAccounts());
                    if (customer.getName().equals(clientName)) {
                        Account.saveAccount(customers, clientName);

                        // if the customer account is empty it don't need to save
                        if (customer.getAccounts() == null) {
                            // show the error message
                            JOptionPane.showMessageDialog(serverUI, "This client don't have" +
                                    " any account", "Error", JOptionPane.ERROR_MESSAGE);
                            Account.saveAccount(customers, clientName);
                            return;
                        } else if (customer.getNumOfAccounts() == 0) {
                            // show the error message
                            JOptionPane.showMessageDialog(serverUI, "This client don't have" +
                                    " any account", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        } else if (customer.getNumOfAccounts() == 2){
                            String showMsg = "Are you sure save the " + clientName + "'s " + accountType + " account?\n" +
                                    "With the changed value:\nBalance: " + balanceField.getText() +
                                    "\nOverDraft: " + overDraftField.getText() +
                                    "\nInterest: " + interestField.getText();

                            // 0 for yes, and 1 for no
                            int n = JOptionPane.showConfirmDialog(serverUI, showMsg, "Save Account", JOptionPane.YES_NO_OPTION);

                            if (n == 0) {
                                for (Customer customer1 : customers) {
                                    if (customer1.getName().equals(clientName)) {
                                        if (accountType.equals("Checking")) {
                                            ((CheckingAccount) customer1.getAccount(0))
                                                    .setBalance(Double.parseDouble(balanceField.getText()));
                                            ((CheckingAccount) customer1.getAccount(0))
                                                    .setOverdraftProtection(Double.parseDouble(overDraftField.getText()));

                                            System.out.println(((CheckingAccount) customer1.getAccount(0)).getBalance());
                                        } else {
                                            ((SavingsAccount) customer1.getAccount(1))
                                                    .setBalance(Double.parseDouble(balanceField.getText()));
                                            ((SavingsAccount) customer1.getAccount(1))
                                                    .setInterestRate(Double.parseDouble(interestField.getText()));
                                        }
                                    }
                                }
                            }

                            // save the account to the local file
                            Account.saveAccount(customers, clientName);


                            // send the changed file to the client
                            for (Socket socket : messageMap.keySet()) {
                                if (messageMap.get(socket).equals(clientName)) {
                                    // todo: un solve - once click change ti send the old file to the client
                                    try {
                                        PrintStream ps = new PrintStream(socket.getOutputStream());
                                        ps.println("#change"); // a special string, show the money is change
                                        new FileServer().sendFileToClient(socket, FindClient.findAccount(clientName).getAbsolutePath());

                                    } catch (IOException e1) {
                                        e1.printStackTrace();
                                    }

                                    //System.out.println("Success");
                                }
                            }
                        }
                    }
                }



            });
        }
    }

    /**
     * A file server, which is send the account file to the client when the
     * client login success
     */
    static class FileServer {
        /**
         * Send the file to client
         *
         * @param s    the client socket
         * @param file the account file
         */
        public void sendFileToClient(Socket s, String file) {
            File f = new File(file);

            try {
                // get the client output stream
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());

                // gey the file read stream
                FileInputStream fis = new FileInputStream(f);

                if (!f.exists()) {
                    String error = "File " + file + " does not exit...\n";
                    int len = error.length();
                    for (int i = 0; i < len; i++)
                        dos.write((int) error.charAt(i));
                    System.out.println(error);
                }

                int length;
                byte[] senData = new byte[1024];

                // write the file name
                dos.writeUTF("Account.txt");

                // write the file
                while ((length = fis.read(senData, 0, senData.length)) > 0) {
                    dos.write(senData, 0, length);
                    dos.flush();
                }
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
//    static class ChangeReceiver extends Thread {
//        Socket clientSocket;
//        BufferedReader br;
//        String Msg;
//
//        public ChangeReceiver(Socket clientSocket) {
//            this.clientSocket = clientSocket;
//        }
//
//        @Override
//        public void run() {
//            try {
//                br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

//    }
}
