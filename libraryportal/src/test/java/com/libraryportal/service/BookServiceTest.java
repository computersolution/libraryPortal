package com.libraryportal.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.libraryportal.entity.Book;
import com.libraryportal.entity.Borrower;
import com.libraryportal.exception.BookNotFoundException;
import com.libraryportal.exception.BorrowerNotFoundException;
import com.libraryportal.exception.DuplicateRegistrationException;
import com.libraryportal.repository.BookRepository;
import com.libraryportal.repository.BorrowerRepository;
import com.libraryportal.util.BookStatus;

class BookServiceTest {

	@Mock
	private BookRepository bookRepository;

	@Mock
	private BorrowerRepository borrowerRepository;

	@InjectMocks
	private BookServiceImpl bookService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testRegisterBook_Success() throws DuplicateRegistrationException {
		Book book = new Book();
		book.setTitle("Test Book");
		book.setIsbn("1234567890");
		book.setAuthor("Test Author");
		book.setStatus(BookStatus.AVAILABLE);

		when(bookRepository.findByIsbnAndTitleAndAuthor(any(), any(), any())).thenReturn(null);
		when(bookRepository.save(any())).thenReturn(book);

		Book registeredBook = bookService.registerBook(book);

		assertNotNull(registeredBook);
		assertEquals("Test Book", registeredBook.getTitle());
		assertEquals("1234567890", registeredBook.getIsbn());
		assertEquals("Test Author", registeredBook.getAuthor());
		assertEquals(1, registeredBook.getNoOfCopies());
		assertEquals(BookStatus.AVAILABLE, registeredBook.getStatus());
	}

	@Test
	void testGetAllBooks() {
		List<Book> books = new ArrayList<>();
		books.add(new Book());
		books.add(new Book());

		when(bookRepository.findAll()).thenReturn(books);

		List<Book> result = bookService.getAllBooks();

		assertNotNull(result);
		assertEquals(2, result.size());
	}

	@Test
	void testBorrowBook_Success() throws BorrowerNotFoundException {
		Long borrowerId = 1L;
		Long bookId = 1L;
		Book book = new Book();
		book.setStatus(BookStatus.AVAILABLE);

		Borrower borrower = new Borrower();

		when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
		when(borrowerRepository.findById(borrowerId)).thenReturn(Optional.of(borrower));

		bookService.borrowBook(borrowerId, bookId);

		assertEquals(BookStatus.BORROWED, book.getStatus());
	}

	@Test
	void testReturnBook_Success() throws BookNotFoundException {
		Long bookId = 1L;
		Book book = new Book();
		book.setStatus(BookStatus.BORROWED);

		when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

		bookService.returnBook(bookId);

		assertEquals(BookStatus.AVAILABLE, book.getStatus());
	}

}
