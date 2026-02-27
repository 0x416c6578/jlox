package com.alex;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.alex.TokenType.*;
import static com.alex.ScannerUtils.*;

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
            // advance the start pointer to the current location
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    static void main() {
        Scanner s = new Scanner("!=())){}-\"Hello, world\" 123.313 12.");
        System.out.println(s.scanTokens().stream()
                .map(Token::toCompactString)
                .collect(Collectors.joining("·")));
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
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
            case '"': string(); break;
            default:
                if (isDigit(c)) {
                    number();
                } else {
                    Lox.error(line, "Unexpected character: " + source.charAt(current));
                }
                break;
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

    /**
     * Return the current character without consuming
     */
    private char peek() {
        return isEof() ? '\0' : source.charAt(current);
    }

    /**
     * Returns the next character without consuming. This language only needs two character lookahead
     */
    private char peekNext() {
        if (current+1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    /**
     * Return the character under the current pointer and consume
     */
    private char advance() {
        return source.charAt(current++);
    }

    /**
     * Consume a string (between ""), handling EOF case
     */
    private void string() {
        while (peek() != '"' && !isEof()) {
            if (peek() == '\n') line++;
            advance();
        }

        if (isEof()) {
            Lox.error(line, "Unterminated string");
            return;
        }

        advance();

        // extract the string literal contents without the quote marks
        addToken(STRING, source.substring(start+1, current-1));
    }

    private void number() {
        while (isDigit(peek())) advance();

        // check to see whether we have a decimal point number
        if (peek() == '.' && isDigit(peekNext())) {
            advance();
            while (isDigit(peek())) advance();
        }

        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
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
