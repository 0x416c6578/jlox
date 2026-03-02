package com.alex.scanner.v3;

import com.alex.scanner.v2.TokenTypeV2;

record Token(TokenType type, Location loc) {
    String lexeme() {
        return type.lexeme();
    }
}
