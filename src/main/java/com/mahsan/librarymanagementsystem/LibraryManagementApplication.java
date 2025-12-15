package com.mahsan.librarymanagementsystem;

import com.mahsan.librarymanagementsystem.cli.LibraryCommandExecutor;
import com.mahsan.librarymanagementsystem.context.AppContext;

public class LibraryManagementApplication {

    private final LibraryCommandExecutor commandExecutor;

    private LibraryManagementApplication(AppContext context) {
        this.commandExecutor = new LibraryCommandExecutor(context);
    }

    public static void main(String[] args) {
        AppContext context = AppContext.defaultContext();
        new LibraryManagementApplication(context).run();
    }

    private void run() {
        boolean runningFlag = true;

        commandExecutor.loadInitialData();

        while (runningFlag) {
            displayMenu();
            String input = commandExecutor.readInput();

            switch (input) {
                case "1":
                    commandExecutor.addItem();
                    break;
                case "2":
                    commandExecutor.removeItem();
                    break;
                case "3":
                    commandExecutor.updateItem();
                    break;
                case "4":
                    commandExecutor.displayBooks();
                    break;
                case "5":
                    commandExecutor.search();
                    break;
                case "6":
                    commandExecutor.sortedList();
                    break;
                case "7":
                    commandExecutor.borrowItem();
                    break;
                case "8":
                    commandExecutor.returnItem();
                    break;
                case "9":
                    commandExecutor.displayBorrowedItems();
                    break;
                case "0":
                    runningFlag = false;
                    commandExecutor.logExit();
                    break;
                default:
                    commandExecutor.logInvalidInput();
                    break;
            }
        }
        commandExecutor.close();
    }

    private static void displayMenu() {
        System.out.println("\n===============================");
        System.out.println(" Library Management System CLI ");
        System.out.println("===============================");
        System.out.println("1.Add new book");
        System.out.println("2.Delete book with title or author");
        System.out.println("3.Update book");
        System.out.println("4.Show all books");
        System.out.println("5.Search");
        System.out.println("6.Sorted list");
        System.out.println("7.Borrow book");
        System.out.println("8.Return book");
        System.out.println("9.Show borrowed books");
        System.out.println("0.Exit");
        System.out.print("Choose a number between 1 and 5 : ");
    }
}
