/*
 * Copyright (c) 18-11-18 下午9:48
 * 李高丞 @版权所有人
 * Version 1.0 Beta
 */

package Data;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * In this class, it contain two static method,
 * one is to lod the client message to the map,
 * and the other is check the ID and password is
 * right of false
 *
 * @author 李高丞
 * @version 1.0 Beta
 */
public class CheckClient {
    /**
     * Load the client message to the map
     *
     * @param filePath where store the client file
     * @return the map which is store all the client message
     */
    public static Map<String, String> loadClientToMap(String filePath) {
        Map<String, String> map = new HashMap<>();

        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(new File(filePath))));

            String str;
            while ((str = br.readLine()) != null) {
                // read the file and split the using the '='
                String[] strs = str.split("=");
                if (strs.length != 2)
                    System.out.println("Wrong type: " + str);

                // put the client ID and password to the map
                map.put(strs[0], strs[1]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * Check the client message
     *
     * @param clientName the client name need to be check
     * @param password   the client password need to be check
     * @return true for right, false for wrong
     */
    public static boolean checkClient(String clientName, String password) {
        Map<String, String> map = loadClientToMap("C:\\Users\\hasee\\Documents\\JavaCode\\The Banking\\ID&Psd.txt");

        for (String name : map.keySet())
            if (clientName.equals(name))
                if (password.equals(map.get(name)))
                    return true;

        return false;
    }
}
