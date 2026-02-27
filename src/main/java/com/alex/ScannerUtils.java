package com.alex;

public class ScannerUtils {
    static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    static boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    static boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }
}
