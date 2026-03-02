package com.alex.scanner.v2;

public sealed interface TokenTypeV2 permits TokenTypeV2.T, TokenTypeV2.Ident, TokenTypeV2.StrLit, TokenTypeV2.NumLit {
    String lexeme();

    enum T implements TokenTypeV2 {
        // Token type constants - use static import: import static TokenTypeV3.T.*
        LEFT_PAREN("("), RIGHT_PAREN(")"), LEFT_BRACE("{"), RIGHT_BRACE("}"), COMMA(","), DOT("."), SEMICOLON(";"),
        PLUS("+"), MINUS("-"), STAR("*"), SLASH("/"),
        BANG("!"), BANG_EQUAL("!="), EQUAL("="), EQUAL_EQUAL("=="), GREATER(">"), GREATER_EQUAL(">="), LESS("<"), LESS_EQUAL("<="),
        AND("and"), CLASS("class"), ELSE("else"), FALSE("false"), FUN("fun"), FOR("for"), IF("if"), NIL("nil"), OR("or"), PRINT("print"), RETURN("return"), SUPER("super"), THIS("this"), TRUE("true"), VAR("var"), WHILE("while"),
        EOF("EOF");

        private final String value;

        T(String value) {
            this.value = value;
        }

        @Override
        public String lexeme() {
            return value;
        }
    }

    record Ident(String name) implements TokenTypeV2 {
        @Override
        public String lexeme() {
            return name;
        }
    }

    record StrLit(String value) implements TokenTypeV2 {
        @Override
        public String lexeme() {
            return value;
        }
    }

    record NumLit(double value) implements TokenTypeV2 {
        @Override
        public String lexeme() {
            return String.valueOf(value);
        }
    }
}
