package com.alex.scanner.v3;

public sealed interface TokenType permits TokenType.T, TokenType.Ident, TokenType.StrLit, TokenType.NumLit {
    String lexeme();

    /// Non-literal / identifier tokens
    enum T implements TokenType {
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

    /// Identifier
    record Ident(String name) implements TokenType {
        @Override
        public String lexeme() {
            return name;
        }
    }

    /// String literal
    record StrLit(String value) implements TokenType {
        @Override
        public String lexeme() {
            return value;
        }
    }

    /// Numerical literal
    record NumLit(double value) implements TokenType {
        @Override
        public String lexeme() {
            return String.valueOf(value);
        }
    }
}