package com.alex.lox;

public record Token(TokenType type, Location loc) {
    String lexeme() {
        return type.lexeme();
    }
}
