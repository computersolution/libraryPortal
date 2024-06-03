package com.libraryportal.service;

import java.util.List;

import com.libraryportal.entity.Book;
import com.libraryportal.entity.BorrowedBookDetails;
import com.libraryportal.exception.BookNotFoundException;
import com.libraryportal.exception.BorrowerNotFoundException;
import com.libraryportal.exception.DuplicateRegistrationException;

public interface BookService {

    Book registerBook(Book book) throws DuplicateRegistrationException;

    List<Book> getAllBooks();

    BorrowedBookDetails borrowBook(Long borrowerId, Long bookId) throws BorrowerNotFoundException;

    Book returnBook(Long bookId) throws BookNotFoundException;

}
