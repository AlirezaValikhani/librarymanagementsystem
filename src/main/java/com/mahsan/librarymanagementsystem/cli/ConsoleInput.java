package com.mahsan.librarymanagementsystem.cli;

import com.mahsan.librarymanagementsystem.io.SystemFileLogger;

import java.util.Scanner;

public final class ConsoleInput {

    private final Scanner scanner;
    private final SystemFileLogger logger;

    public ConsoleInput(Scanner scanner, SystemFileLogger logger) {
        this.scanner = scanner;
        this.logger = logger;
    }

    public String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public String readTrimmedLine(String prompt) {
        return readLine(prompt).trim();
    }

    public Integer readInt(String prompt, String invalidMessage) {
        String input = readTrimmedLine(prompt);
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            logger.logAction("Error", invalidMessage);
            return null;
        }
    }

    public Boolean readBooleanStrict(String prompt, String invalidMessage) {
        String input = readTrimmedLine(prompt);
        if (input.equalsIgnoreCase("true"))
            return true;
        if (input.equalsIgnoreCase("false"))
            return false;

        logger.logAction("Error", invalidMessage);
        return null;
    }
}
