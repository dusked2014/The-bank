/*
 * Copyright (c) 18-11-19 下午7:15
 * 李高丞 @版权所有人
 * Version 1.0 Beta
 */

package Data;

import java.io.File;
import java.io.IOException;

/**
 * In this class it write a static method to find the client file
 *
 * @author 李高丞
 * @version 1.0 Beta
 */
public class FindClient {
    /**
     * The static method that find the client account file
     *
     * @param ClientName the client name
     * @return the account file
     */
    public static File findAccount(String ClientName) {

        // the client account file path
        String dirPath = "C:\\Users\\hasee\\Documents\\JavaCode" +
                "\\The Banking\\src\\Data\\Client\\" + ClientName;
        String filePath = dirPath + "\\Account.txt";

        File file = new File(dirPath);
        File file1 = new File(filePath);

        try {
            if (!file.exists()) file.mkdirs();
            if (!file1.exists()) file1.createNewFile();
        } catch (IOException e) {
            System.out.println("Create file fail.");
        }

        return file1;
    }

    public static File findCustomer(String clientName) {

        String dirPath = "C:\\Users\\hasee\\Documents\\JavaCode" +
                "\\The Banking\\src\\Data\\Client\\" + clientName;

        File file = new File(dirPath);

        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
}
