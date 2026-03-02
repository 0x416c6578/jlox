package com.alex.scanner.v2;

record TokenV2(TokenTypeV3 type, Location loc) {
    public record Location(int line, int offset) {}
    public sealed interface TokenTypeV3 permits TokenTypeV3.Operator, TokenTypeV3.NumberLit {
        enum Operator implements TokenTypeV3 {
            PLUS, MINUS
        }
        record NumberLit(int number) implements TokenTypeV3 {}
    }
}
