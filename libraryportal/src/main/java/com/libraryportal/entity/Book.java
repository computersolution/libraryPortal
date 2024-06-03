package com.libraryportal.entity;

import com.libraryportal.util.BookStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    
    private String isbn;
    private String title;
    private String author;
    
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private int noOfCopies; // Number of copies of the book available
    
    @Enumerated(EnumType.STRING)
    private BookStatus status; // Status of the book (Available, Borrowed)
    
    /**
     * Get the unique ID of the book.
     * 
     * @return The ID of the book.
     */
    public Long getId() {
        return id;
    }

    /**
     * Set the unique ID of the book.
     * 
     * @param id The ID of the book.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Get the ISBN (International Standard Book Number) of the book.
     * 
     * @return The ISBN of the book.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Set the ISBN (International Standard Book Number) of the book.
     * 
     * @param isbn The ISBN of the book.
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Get the title of the book.
     * 
     * @return The title of the book.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title of the book.
     * 
     * @param title The title of the book.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the author of the book.
     * 
     * @return The author of the book.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Set the author of the book.
     * 
     * @param author The author of the book.
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Get the number of copies of the book available.
     * 
     * @return The number of copies of the book available.
     */
    public int getNoOfCopies() {
        return noOfCopies;
    }

    /**
     * Set the number of copies of the book available.
     * 
     * @param noOfCopies The number of copies of the book available.
     */
    public void setNoOfCopies(int noOfCopies) {
        this.noOfCopies = noOfCopies;
    }

    /**
     * Get the status of the book.
     * 
     * @return The status of the book.
     */
    public BookStatus getStatus() {
        return status;
    }

    /**
     * Set the status of the book.
     * 
     * @param status The status of the book.
     */
    public void setStatus(BookStatus status) {
        this.status = status;
    }
}
