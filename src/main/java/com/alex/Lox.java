package com.alex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

class Lox {
    static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("run jlox [script.jl]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    // run a lox file
    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset());
    }

    // start interactive lox prompt
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (;;) {
            System.out.println("> ");
            String line = reader.readLine();
            if (line == null) break;
            run(line);
        }
    }

    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> ts = scanner.scanTokens();

        for (Token t : ts) {
            System.out.println(t);
        }
    }

    // stubbed classes
    private static class Scanner {
        public Scanner(String source) {}

        public List<Token> scanTokens() {
            return List.of();
        }
    }

    private static class Token {}
}