package com.libraryportal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.libraryportal.entity.Book;
import com.libraryportal.exception.BookNotFoundException;
import com.libraryportal.exception.BorrowerNotFoundException;
import com.libraryportal.exception.DuplicateRegistrationException;
import com.libraryportal.exception.ErrorResponse;
import com.libraryportal.service.BookServiceImpl;

class BookControllerTest {

	@Mock
	private BookServiceImpl bookService;

	@InjectMocks
	private BookController bookController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testRegisterBook_Success() throws DuplicateRegistrationException {
		Book book = new Book();
		book.setTitle("Test Book");
		book.setAuthor("Test Author");

		when(bookService.registerBook(any(Book.class))).thenReturn(book);

		ResponseEntity<?> response = bookController.registerBook(book);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(book, response.getBody());
	}

	@Test
	void testRegisterBook_DuplicateRegistration() throws DuplicateRegistrationException {
		Book book = new Book();
		book.setTitle("Test Book");
		book.setAuthor("Test Author");

		doThrow(DuplicateRegistrationException.class).when(bookService).registerBook(any(Book.class));

		ResponseEntity<?> response = bookController.registerBook(book);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

		assertTrue(response.getBody() instanceof ErrorResponse);

		ErrorResponse errorResponse = (ErrorResponse) response.getBody();
		assertEquals(400, errorResponse.getErrorCode());
	}

	@Test
	void testGetAllBooks_Success() {
		List<Book> books = new ArrayList<>();
		books.add(new Book());
		books.add(new Book());

		when(bookService.getAllBooks()).thenReturn(books);

		ResponseEntity<List<Book>> response = bookController.getAllBooks();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(books, response.getBody());
	}

	@Test
	void testBorrowBook_Success() throws BorrowerNotFoundException {
		ResponseEntity<?> response = bookController.borrowBook(1L, 1L);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void testReturnBook_Success() throws BookNotFoundException {
		ResponseEntity<?> response = bookController.returnBook(1L);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testHandleDuplicateRegistrationException() {
		try {
			when(bookService.registerBook(any())).thenThrow(DuplicateRegistrationException.class);
			ResponseEntity<?> response = new BookController(bookService).registerBook(null);
			assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		} catch (DuplicateRegistrationException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testHandleBorrowerNotFoundException() {
		try {
			when(bookService.borrowBook(anyLong(), anyLong())).thenThrow(BorrowerNotFoundException.class);
			ResponseEntity<?> response = new BookController(bookService).borrowBook(1L, 2L);
			assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		} catch (BorrowerNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testHandleBookNotFoundException() {
		try {
			when(bookService.returnBook(anyLong())).thenThrow(BookNotFoundException.class);
			ResponseEntity<?> response = new BookController(bookService).returnBook(1L);
			assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		} catch (BookNotFoundException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testGetAllBooksCatchBlock() {
		when(bookService.getAllBooks()).thenThrow(new RuntimeException("Test Exception"));
		BookController bookController = new BookController(bookService);
		ResponseEntity<List<Book>> responseEntity = bookController.getAllBooks();
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
		assertEquals(null, responseEntity.getBody());
	}

	@Test
	public void testBorrowBookCatchBlock() {
		try {
			when(bookService.borrowBook(anyLong(), anyLong())).thenThrow(new RuntimeException("Test Exception"));
			BookController bookController = new BookController(bookService);
			ResponseEntity<?> responseEntity = bookController.borrowBook(1L, 2L);
			assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
			assertEquals("An error occurred while processing the request.", responseEntity.getBody());
		} catch (BorrowerNotFoundException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testReturnBookCatchBlock() {
		try {
			when(bookService.returnBook(anyLong())).thenThrow(new RuntimeException("Test Exception"));
			BookController bookController = new BookController(bookService);
			ResponseEntity<?> responseEntity = bookController.returnBook(1L);
			assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
			assertEquals("An error occurred while processing the request.", responseEntity.getBody());
		} catch (BookNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testRegisterBookCatchBlock() {
		try {
			when(bookService.registerBook(Mockito.any())).thenThrow(new RuntimeException("Test Exception"));
			BookController bookController = new BookController(bookService);
			ResponseEntity<?> responseEntity = bookController.registerBook(new Book());
			assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
			assertEquals(null, responseEntity.getBody());
		} catch (DuplicateRegistrationException e) {
			e.printStackTrace();
		}
	}

}
