package com.pivotallabs.util;

public class Strings {
    public static boolean isEmptyOrWhitespace(String s) {
        return (s == null) || (s.trim().length() == 0);
    }
}
