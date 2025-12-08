package com.mahsan.librarymanagementsystem.io;

import com.mahsan.librarymanagementsystem.enums.BookState;
import com.mahsan.librarymanagementsystem.exception.LogWriteException;
import com.mahsan.librarymanagementsystem.exception.MissingParametersException;
import com.mahsan.librarymanagementsystem.model.Book;
import com.mahsan.librarymanagementsystem.model.Library;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileHandler {

    private static final String INPUT_FILE = "books_input.txt";
    private static final String OUTPUT_FILE = "log_output.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public int loadBooksFromFile(Library library) {

        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                String[] bookDetails = line.split(",");
                if (bookDetails.length != 4) {
                    logAction("Error", "This line has more or less than 4 columns : " + line);
                    continue;
                }

                try {
                    String title = bookDetails[0].trim();
                    String author = bookDetails[1].trim();
                    int yearOfPublication = Integer.parseInt(bookDetails[2].trim());
                    int bookStateValue = Integer.parseInt(bookDetails[3].trim());
                    BookState bookState = BookState.fromValue(bookStateValue);

                    Book book = new Book(title, author, yearOfPublication, bookState);
                    library.addBook(book);
                    count++;
                } catch (NumberFormatException e) {
                    logAction("Error", "Invalid year of publication : " + line);
                    throw new MissingParametersException("Invalid year of publication : " + line);
                }

            }

        } catch (FileNotFoundException e) {
            logAction("Load error", "File not found : " + INPUT_FILE);
            throw new com.mahsan.librarymanagementsystem.exception.FileNotFoundException("File not found");
        } catch (IOException e) {
            logAction("Error", "IO error : " + e.getMessage());
            throw new com.mahsan.librarymanagementsystem.exception.IOException("Error loading file : " + e.getMessage());
        }

        return count;
    }

    public void logAction(String operation, String result) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String log = String.format("[%s] Operation : %s | Result : %s", timestamp, operation, result);

        try {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_FILE, true))) {
                bw.write(log);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new LogWriteException("Error in writing output file!");
        }
    }
}
