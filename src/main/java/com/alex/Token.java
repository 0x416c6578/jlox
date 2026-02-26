package com.alex;

public class Token {
    final TokenType type;
    final String lexeme;
    final Object literal;
    final int line;;

    public Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    @Override
    public String toString() {
        return String.format("Tok{ %s '%s' %s }", type, lexeme, literal);
    }

    public String toCompactString() {
        if (type == TokenType.EOF)
            return "EOF";
        return lexeme;
    }
}
