package com.libraryportal.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.libraryportal.entity.Book;
import com.libraryportal.entity.BorrowedBookDetails;
import com.libraryportal.exception.BookNotFoundException;
import com.libraryportal.exception.BorrowerNotFoundException;
import com.libraryportal.exception.DuplicateRegistrationException;
import com.libraryportal.exception.ErrorResponse;
import com.libraryportal.service.BookService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/books")
public class BookController {
	private static final Logger logger = LoggerFactory.getLogger(BookController.class);

	private final BookService bookService;

	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	/**
	 * Endpoint to register a new book.
	 * 
	 * @param book The book object to register.
	 * @return ResponseEntity containing the registered book if successful, or an
	 *         error response if a duplicate registration occurs or an exception
	 *         occurs.
	 */
	@PostMapping("/registerbook")
	public ResponseEntity<?> registerBook(@RequestBody Book book) {
		try {
			Book registeredBook = bookService.registerBook(book);
			return ResponseEntity.status(HttpStatus.CREATED).body(registeredBook);
		} catch (DuplicateRegistrationException e) {
			ErrorResponse errorResponse = new ErrorResponse(400, e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
		} catch (Exception e) {
			logger.error("Error registering book", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	/**
	 * Endpoint to get all books.
	 * 
	 * @return ResponseEntity containing a list of all books if successful, or an
	 *         error response if an exception occurs.
	 */
	@GetMapping("/getBooks")
	public ResponseEntity<List<Book>> getAllBooks() {
		try {
			List<Book> books = bookService.getAllBooks();
			return ResponseEntity.ok(books);
		} catch (Exception e) {
			logger.error("Error fetching all books", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	/**
	 * Endpoint to borrow a book.
	 * 
	 * @param borrowerId The ID of the borrower.
	 * @param bookId     The ID of the book to borrow.
	 * @return ResponseEntity indicating success if successful, or an error response
	 *         if the borrower or book is not found or an exception occurs.
	 */
	@PutMapping("/{bookId}/{borrowerId}/borrow")
	public ResponseEntity<?> borrowBook(@PathVariable Long bookId, @PathVariable Long borrowerId) {
		try {
			BorrowedBookDetails response = bookService.borrowBook(borrowerId, bookId);
			return ResponseEntity.ok(response);
		} catch (BorrowerNotFoundException | EntityNotFoundException e) {
			ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
			return ResponseEntity.badRequest().body(errorResponse);
		} catch (Exception e) {
			logger.error("Error borrowing book with ID: {}", bookId, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while processing the request.");
		}
	}

	/**
	 * Endpoint to return a book.
	 * 
	 * @param bookId The ID of the book to return.
	 * @return ResponseEntity indicating success if successful, or an error response
	 *         if the book is not found or an exception occurs.
	 */
	@PutMapping("/{bookId}/return")
	public ResponseEntity<?> returnBook(@PathVariable Long bookId) {
		try {
			Book returnedBook = bookService.returnBook(bookId);
			return ResponseEntity.status(HttpStatus.OK).body(returnedBook);
		} catch (BookNotFoundException | EntityNotFoundException e) {
			ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
			return ResponseEntity.badRequest().body(errorResponse);
		} catch (Exception e) {
			logger.error("Error returning book with ID: {}", bookId, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while processing the request.");
		}
	}

}
