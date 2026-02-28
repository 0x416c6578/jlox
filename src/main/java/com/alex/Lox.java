package com.alex;

import com.alex.scanner.Scanner;
import com.alex.scanner.Token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

class Lox {
    static boolean hadError = false;

    static void main(String[] args) throws IOException {
        if (args.length > 1) {
            IO.println("run jlox [script.jl]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    /// Run a file
    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));

        if (hadError) System.exit(65);
    }

    /// Start interactive prompt
    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for (;;) {
            IO.println("> ");
            String line = reader.readLine();
            if (line == null) break;
            run(line);
            hadError = false;
        }
    }

    /// Run source code
    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        var scanResult = scanner.scanTokens();

        // Print any observed errors
        IO.print(scanResult.errors().stream()
                .map(Lox::formatScanErr)
                .collect(Collectors.joining("\n")) +
                (scanResult.errors().isEmpty() ? "" : "\n"));

        IO.println((scanResult.tokens().stream()
                .map(Token::toCompactString)
                .collect(Collectors.joining("·"))));
    }

    static String formatScanErr(Scanner.ScanError e) {
        return String.format("Scan error [%d:%d]: %s", e.loc().line(), e.loc().offset(), e.message());
    }
}