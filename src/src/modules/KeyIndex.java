/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modules;

/**
 *
 * @author Soap
 */
public class KeyIndex {
    private static int key = 0;
    private static int apptKey = 0;

    public static int getApptKey() {
        return apptKey;
    }

    public static void setApptKey(int apptKey) {
        KeyIndex.apptKey = apptKey;
    }

    public static int getKey() {
        return key;
    }

    public static void setKey(int key) {
        KeyIndex.key = key;
    }
    
    public static void incKey() {
        key++;
    }
    public static void decKey() {
        key--;
    }
    
}
