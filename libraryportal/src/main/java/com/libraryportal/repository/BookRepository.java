package com.libraryportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.libraryportal.entity.Book;

/**
 * Repository interface for accessing book data in the database.
 */
public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Find a book by its ISBN.
     * 
     * @param isbn The ISBN of the book to find.
     * @return The book with the specified ISBN, or null if not found.
     */
    Book findByIsbn(String isbn);

    /**
     * Find a book by its ISBN, title, and author.
     * 
     * @param isbn The ISBN of the book to find.
     * @param title The title of the book to find.
     * @param author The author of the book to find.
     * @return The book with the specified ISBN, title, and author, or null if not found.
     */
    Book findByIsbnAndTitleAndAuthor(String isbn, String title, String author);
}
