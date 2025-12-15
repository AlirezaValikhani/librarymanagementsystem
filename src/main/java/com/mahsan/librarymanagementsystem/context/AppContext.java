package com.mahsan.librarymanagementsystem.context;

import com.mahsan.librarymanagementsystem.factory.LibraryItemFactory;
import com.mahsan.librarymanagementsystem.io.FileHandler;
import com.mahsan.librarymanagementsystem.model.LibraryManager;

import java.util.Scanner;

public class AppContext {
    private final FileHandler fileHandler;
    private final LibraryManager manager;
    private final LibraryItemFactory factory;
    private final Scanner scanner;

    public AppContext(FileHandler fileHandler, LibraryManager manager, LibraryItemFactory factory, Scanner scanner) {
        this.fileHandler = fileHandler;
        this.manager = manager;
        this.factory = factory;
        this.scanner = scanner;
    }

    public static AppContext defaultContext() {
        FileHandler fileHandler = new FileHandler();
        LibraryManager manager = new LibraryManager(fileHandler);
        LibraryItemFactory factory = new LibraryItemFactory();
        Scanner scanner = new Scanner(System.in);
        return new AppContext(fileHandler, manager, factory, scanner);
    }

    public FileHandler getFileHandler() {
        return fileHandler;
    }

    public LibraryManager getManager() {
        return manager;
    }

    public LibraryItemFactory getFactory() {
        return factory;
    }

    public Scanner getScanner() {
        return scanner;
    }
}
