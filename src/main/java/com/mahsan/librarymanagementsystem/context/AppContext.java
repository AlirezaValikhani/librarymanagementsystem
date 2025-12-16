package com.mahsan.librarymanagementsystem.context;

import com.mahsan.librarymanagementsystem.io.CsvDataLoader;
import com.mahsan.librarymanagementsystem.io.SystemFileLogger;
import com.mahsan.librarymanagementsystem.model.LibraryManager;

import java.util.Scanner;

public class AppContext {
    private final SystemFileLogger logger;
    private final CsvDataLoader csvDataLoader;
    private final LibraryManager manager;
    private final Scanner scanner;

    public AppContext(SystemFileLogger logger, CsvDataLoader csvDataLoader, LibraryManager manager, Scanner scanner) {
        this.logger = logger;
        this.csvDataLoader = csvDataLoader;
        this.manager = manager;
        this.scanner = scanner;
    }

    public static AppContext defaultContext() {
        SystemFileLogger logger = new SystemFileLogger();
        CsvDataLoader csvDataLoader = new CsvDataLoader(logger);
        LibraryManager manager = new LibraryManager(logger);
        Scanner scanner = new Scanner(System.in);
        return new AppContext(logger, csvDataLoader, manager, scanner);
    }

    public CsvDataLoader getCsvDataLoader() {
        return csvDataLoader;
    }

    public SystemFileLogger getLogger() {
        return logger;
    }

    public LibraryManager getManager() {
        return manager;
    }

    public Scanner getScanner() {
        return scanner;
    }
}
