package com.fraktalis.caterpillapp.leafguard_core.util;

/**
 * Created by vincentale on 29/01/18.
 */

public class StringHelper {

    /**
     * Set the first letter of the line to capital letter
     * @param line
     * @return
     */
    public static String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    /**
     * Reverse operation from @{@link java.util.Arrays} toString() method
     * @param s
     * @return
     */
    public static String[] parse(String s) {
String listString = s.substring(1, s.length() - 1); // chop off brackets
        String[] output = listString.split(", ");
        return output;
    }
}
