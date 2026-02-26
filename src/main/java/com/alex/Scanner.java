package com.alex;

import java.util.ArrayList;
import java.util.List;

import static com.alex.TokenType.*;

/**
 * Scanner for the Lox interpreter
 */
public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0; // index into source to the first char in the current lexeme being scanned
    private int current = 0; // index into source to the character currently being considered
    private int line = 1; // line number we are currently on

    Scanner(String source) {
        this.source = source;
    }

    List<Token> scanTokens() {
        while (!isEof()) {
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private void scanToken() {
        switch (advance()) {
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '{': addToken(LEFT_BRACE); break;
            case '}': addToken(RIGHT_BRACE); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case '-': addToken(MINUS); break;
            case '+': addToken(PLUS); break;
            case ';': addToken(SEMICOLON); break;
            case '*': addToken(STAR); break;
            default: Lox.error(line, "Unexpected character: " + source.charAt(current));
        }
    }

    private char advance() {
        return source.charAt(current++);
    }

    private void addToken(TokenType t) {
        addToken(t, null);
    }

    private void addToken(TokenType t, Object lit) {
        String text = source.substring(start, current);
        tokens.add(new Token(t, text, lit, line));
    }

    private boolean isEof() {
        return current >= source.length();
    }
}
