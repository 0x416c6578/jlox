package com.alex;

import static com.alex.TokenType.*;

public record Token(TokenType type, String lexeme, Object literal, int line) {
    @Override
    public String toString() {
        return String.format("Tok{ %s '%s' %s }", type, lexeme, literal);
    }

    public String toCompactString() {
        if (type == EOF)
            return "EOF";
        if (type == IDENTIFIER)
                return String.format("I<%s>", lexeme);
        return lexeme;
    }
}
