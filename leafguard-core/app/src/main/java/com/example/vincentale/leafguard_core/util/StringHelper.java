package com.example.vincentale.leafguard_core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by vincentale on 29/01/18.
 */

public class StringHelper {

    public static String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    public static String[] parse(String s) {
String listString = s.substring(1, s.length() - 1); // chop off brackets
        String[] output = listString.split(", ");
        return output;
    }
}
