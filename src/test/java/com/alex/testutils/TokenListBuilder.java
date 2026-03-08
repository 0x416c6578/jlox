package com.alex.testutils;

import com.alex.lox.Location;
import com.alex.lox.Token;
import com.alex.lox.TokenType;

import java.util.ArrayList;
import java.util.List;

public class TokenListBuilder {
    List<Token> tokens = new ArrayList<>();

    private TokenListBuilder() {}

    public TokenListBuilder t(TokenType type, int line, int offset) {
        tokens.add(new Token(type, new Location(line, offset)));
        return this;
    }

    public static TokenListBuilder tlb() {
        return new TokenListBuilder();
    }

    public List<Token> b() {
        return tokens;
    }
}
