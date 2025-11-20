package com.library.models;

import java.io.Serializable;

public class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String isbn;
    private String title;
    private String author;
    private String genre;
    private String publisher;
    private int publicationYear;
    private int totalCopies;
    private int availableCopies;
    private String description;

    public Book(String isbn, String title, String author, String genre, String publisher, 
                int publicationYear, int totalCopies, String description) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
        this.description = description;
    }

    // Getters and setters
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getGenre() { return genre; }
    public String getPublisher() { return publisher; }
    public int getPublicationYear() { return publicationYear; }
    public int getTotalCopies() { return totalCopies; }
    public int getAvailableCopies() { return availableCopies; }
    public String getDescription() { return description; }
    public boolean isAvailable() { return availableCopies > 0; }
    
    public void setAvailableCopies(int availableCopies) { 
        this.availableCopies = availableCopies;
    }
    
    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return title + " by " + author + " (" + publicationYear + ")";
    }
}