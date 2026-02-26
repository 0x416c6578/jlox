package com.alex;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    static void main() {
        Scanner s = new Scanner("!=())){}-");
        System.out.println(s.scanTokens().stream()
                .map(Token::toCompactString)
                .collect(Collectors.joining(" ")));
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
            case '!': addToken(readAheadMatch('=') ? BANG_EQUAL : BANG); break;
            case '=': addToken(readAheadMatch('=') ? EQUAL_EQUAL : EQUAL); break;
            case '<': addToken(readAheadMatch('=') ? LESS_EQUAL : LESS); break;
            case '>': addToken(readAheadMatch('=') ? GREATER_EQUAL : GREATER); break;
            case '/':
                if (readAheadMatch('/'))
                    while (peek() != '\n' && !isEof()) advance();
                else
                    addToken(SLASH);
                break;
            case ' ':
            case '\r':
            case '\t':
                break; // ignore all whitespace
            case '\n': line++; break; // increment line counter when we hit a newline
            default: Lox.error(line, "Unexpected character: " + source.charAt(current));
        }
    }

    /**
     * Consume the next token conditionally on whether it matches expected, used for double-character lexemes
     * @param expected Expected character to see next
     * @return true if we matched
     */
    private boolean readAheadMatch(char expected) {
        if (isEof()) return false;
        if (source.charAt(current) != expected) return false;

        advance();
        return true;
    }

    private char peek() {
        if (isEof()) return '\0';
        return source.charAt(current);
    }

    /**
     * Return the character under the current pointer, then advance the current pointer
     */
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

    /**
     * Returns true if we have hit the end of the source
     */
    private boolean isEof() {
        return current >= source.length();
    }
}
