/*
 * Copyright (c) 18-11-18 下午9:57
 * 李高丞 @版权所有人
 * Version 1.0 Beta
 */

package Data;


import java.io.File;

/**
 * In this class, it contain two static methods, one is delete the client's account
 * and the other is delete the client folder.
 *
 * @author 李高丞
 * @version 1.0 Beta
 */
public class DeleteFile {

    /**
     * Delete the client account using the client name
     * @param clientName the client name that need to delete the account
     */
    public static void deleteAccount(String clientName) {
        // find the client account file
        File file = FindClient.findAccount(clientName);

        for (int i = 0; i < 9; i++) {
            // delete the file
            boolean result = file.getAbsoluteFile().delete();

            if (!result) {
                // if don't success, notify the JVM
                // and then try delete again
                System.gc();
                file.delete();
            }
        }
    }

    public static void deleteCustomer(String clientName) {
        // find the client folder
        File file = FindClient.findCustomer(clientName);

        // file the client account
        File account = FindClient.findAccount(clientName);

        // if the client folder has the account, then delete the account file first
        if (account.exists())
            deleteAccount(clientName);

        for (int i = 0; i < 9; i++) {
            // delete the folder
            boolean flag = file.getAbsoluteFile().delete();

            if (!flag) {
                // if don't success, notify the JVM
                // and then try delete again
                System.gc();
                file.delete();
            }
        }
    }
}
