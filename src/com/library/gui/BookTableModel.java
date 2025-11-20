package com.library.gui;

import com.library.models.Book;
import javax.swing.table.AbstractTableModel;
import java.util.List;

public class BookTableModel extends AbstractTableModel {
    private List<Book> books;
    private final String[] columnNames = {"ISBN", "Title", "Author", "Genre", "Publisher", "Year", "Available", "Total"};

    public BookTableModel(List<Book> books) {
        this.books = books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public int getRowCount() {
        return books.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Book book = books.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return book.getIsbn();
            case 1:
                return book.getTitle();
            case 2:
                return book.getAuthor();
            case 3:
                return book.getGenre();
            case 4:
                return book.getPublisher();
            case 5:
                return book.getPublicationYear();
            case 6:
                return book.getAvailableCopies();
            case 7:
                return book.getTotalCopies();
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        // Return Integer.class for numeric columns (year, available, total)
        if (columnIndex == 5 || columnIndex == 6 || columnIndex == 7) {
            return Integer.class;
        }
        return String.class;
    }
}