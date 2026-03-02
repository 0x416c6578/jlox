package com.alex.scanner.v3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.alex.scanner.ScannerUtils.*;
import static com.alex.scanner.v3.TokenType.T.*;

/// Scanner for the Lox interpreter. The tokens of this language are found in [com.alex.scanner.TokenType]
public class ScannerV3 {
    public record ScanError(Location loc, String message) {
        @Override
        public String toString() {
            return String.format("Scan error [%d:%d]: %s", loc.line(), loc.offset(), message);
        }
    }

    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private final List<ScanError> errors = new ArrayList<>();

    private int start = 0; // index into source to the first char in the current lexeme being scanned
    private int current = 0; // index into source to the character currently being considered
    private int line = 1; // line number we are currently on
    private int lineOffset = 0; // offset into the current line, used for error reporting

    public ScannerV3(String source) {
        this.source = source;
    }

    public record ScanResult(List<Token> tokens, List<ScanError> errors) {}

    static void main() {
        var s = new ScannerV3("()hello world 123 123.45 \"string literal\" if else ! != = == < <= > >= / // comment");

        var r = s.scanTokens();

        // Print any observed errors
        IO.print(r.errors().stream()
                .map(ScanError::toString)
                .collect(Collectors.joining("\n")) +
                (r.errors().isEmpty() ? "" : "\n"));

        IO.println((r.tokens().stream()
                .map(Token::lexeme)
                .collect(Collectors.joining("·"))));
    }

    public ScanResult scanTokens() {
        while (!isEof()) {
            // advance the start pointer to the current location
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, new Location(line, lineOffset)));
        return new ScanResult(List.copyOf(tokens), List.copyOf(errors));
    }

    /// Scans source for the next token
    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(' -> addToken(LEFT_PAREN);
            case ')' -> addToken(RIGHT_PAREN);
            case '{' -> addToken(LEFT_BRACE);
            case '}' -> addToken(RIGHT_BRACE);
            case ',' -> addToken(COMMA);
            case '.' -> addToken(DOT);
            case '-' -> addToken(MINUS);
            case '+' -> addToken(PLUS);
            case ';' -> addToken(SEMICOLON);
            case '*' -> addToken(STAR);
            case '!' -> addToken(readAheadMatch('=') ? BANG_EQUAL : BANG);
            case '=' -> addToken(readAheadMatch('=') ? EQUAL_EQUAL : EQUAL);
            case '<' -> addToken(readAheadMatch('=') ? LESS_EQUAL : LESS);
            case '>' -> addToken(readAheadMatch('=') ? GREATER_EQUAL : GREATER);
            case '/' -> commentOrSlash();
            case '\n' -> newLine();
            case '"' -> stringLiteral();
            case ' ', '\t', '\r' -> { /* Ignore whitespace */ }
            default -> {
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    errors.add(new ScanError(new Location(line, lineOffset), "Unexpected character: " + c));
                }
            }
        }
    }

    /// Handle new line, incrementing line number and resetting line offset
    private void newLine() {
        lineOffset = 0;
        line++;
    }

    /// Handle comment or slash alternative
    private void commentOrSlash() {
        if (readAheadMatch('/'))
            while (peek() != '\n' && !isEof()) advance();
        else
            addToken(SLASH);
    }

    /// Consume the next token conditionally on whether it matches expected, used for double-character lexemes
    /// @param expected Expected character to see next
    /// @return true if we matched
    private boolean readAheadMatch(char expected) {
        if (isEof()) return false;
        if (source.charAt(current) != expected) return false;

        advance();
        return true;
    }

    /// Return the current character without consuming
    private char peek() {
        return isEof() ? '\0' : source.charAt(current);
    }

    /// Returns the next character without consuming. This language only needs two character lookahead
    private char peekNext() {
        if (current+1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    /// Return the character under the current pointer and consume
    private char advance() {
        lineOffset++;
        return source.charAt(current++);
    }

    /// Consume a string (between ""), handling EOF case for unterminated string
    private void stringLiteral() {
        while (peek() != '"' && !isEof()) {
            if (peek() == '\n') newLine();
            advance();
        }

        if (isEof()) {
            errors.add(new ScanError(new Location(line, lineOffset), "Unterminated string literal"));
            return;
        }

        advance();

        // extract the string literal contents without the quote marks
        addToken(new StrLit(source.substring(start+1, current-1)));
    }

    /// Consume a numerical literal
    private void number() {
        while (isDigit(peek())) advance();

        // check to see whether we have a decimal point number
        if (peek() == '.' && isDigit(peekNext())) {
            do advance(); while (isDigit(peek()));
        }

        addToken(new NumLit(Double.parseDouble(source.substring(start, current))));
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) advance();
        String name = source.substring(start, current);
        addToken(RESERVED_WORDS.getOrDefault(name, new Ident(name)));
    }

    private void addToken(TokenType t) {
        tokens.add(new Token(t, new Location(line, lineOffset)));
    }

    /// Returns true if we have hit the end of the source
    private boolean isEof() {
        return current >= source.length();
    }

    static Map<String, TokenType> RESERVED_WORDS = Map.ofEntries(
            Map.entry("and", AND),
            Map.entry("class", CLASS),
            Map.entry("else", ELSE),
            Map.entry("false", FALSE),
            Map.entry("for", FOR),
            Map.entry("fun", FUN),
            Map.entry("if", IF),
            Map.entry("nil", NIL),
            Map.entry("or", OR),
            Map.entry("print", PRINT),
            Map.entry("return", RETURN),
            Map.entry("super", SUPER),
            Map.entry("this", THIS),
            Map.entry("true", TRUE),
            Map.entry("var", VAR),
            Map.entry("while", WHILE)
    );
}