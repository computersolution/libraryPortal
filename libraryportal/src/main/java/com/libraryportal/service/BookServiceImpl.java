package com.libraryportal.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.libraryportal.entity.Book;
import com.libraryportal.entity.BorrowedBookDetails;
import com.libraryportal.entity.Borrower;
import com.libraryportal.exception.BookNotFoundException;
import com.libraryportal.exception.BorrowerNotFoundException;
import com.libraryportal.exception.DuplicateRegistrationException;
import com.libraryportal.repository.BookRepository;
import com.libraryportal.repository.BorrowerRepository;
import com.libraryportal.util.BookStatus;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

/**
 * Service class providing methods to manage books in the library.
 */
@Service
public class BookServiceImpl implements BookService {
	private static final Logger LOGGER = LoggerFactory.getLogger(BookService.class);

	private final BookRepository bookRepository;
	private final BorrowerRepository borrowerRepository;

	/**
	 * Constructor for BookService.
	 * 
	 * @param bookRepository     Repository for managing books.
	 * @param borrowerRepository Repository for managing borrowers.
	 */
	public BookServiceImpl(BookRepository bookRepository, BorrowerRepository borrowerRepository) {
		this.bookRepository = bookRepository;
		this.borrowerRepository = borrowerRepository;
	}

	/**
	 * Register a new book.
	 * 
	 * @param book The book to register.
	 * @return The registered book.
	 * @throws DuplicateRegistrationException If a book with the same ISBN, title,
	 *                                        and author already exists.
	 */
	@Transactional
	public Book registerBook(Book book) throws DuplicateRegistrationException {
		LOGGER.info("Registering a new book: {}", book.getTitle());
		validateBook(book);

		// Check if a book with the same ISBN, title, and author already exists
		Book existingBook = bookRepository.findByIsbnAndTitleAndAuthor(book.getIsbn(), book.getTitle(),
				book.getAuthor());
		if (existingBook != null) {
			// If a book with the same ISBN, title, and author exists, increment the number
			// of copies
			existingBook.setNoOfCopies(existingBook.getNoOfCopies() + 1);
			LOGGER.info("Incrementing copies of existing book: {}", existingBook.getTitle());
			return bookRepository.save(existingBook);
		}

		// Save the book to the database with initial number of copies as 1
		book.setNoOfCopies(1);
		LOGGER.info("Saving new book to database: {}", book.getTitle());
		return bookRepository.save(book);
	}

	/**
	 * Get all books.
	 * 
	 * @return List of all books.
	 */
	public List<Book> getAllBooks() {
		LOGGER.info("Fetching all books from the database");
		return bookRepository.findAll();
	}

	/**
	 * Validate the book before registration.
	 * 
	 * @param book The book to validate.
	 * @throws DuplicateRegistrationException If the book is not valid for
	 *                                        registration.
	 */
	private void validateBook(Book book) throws DuplicateRegistrationException {
		LOGGER.info("Validating book for registration: {}", book.getTitle());
		// Perform validation logic here
		if (book.getIsbn() == null || book.getIsbn().isEmpty()) {
			LOGGER.error("ISBN is required for book registration");
			throw new DuplicateRegistrationException("ISBN number is required.");
		}
	}

	/**
	 * Borrow a book.
	 * 
	 * @param borrowerId The ID of the borrower.
	 * @param bookId     The ID of the book to borrow.
	 * @throws BorrowerNotFoundException If the borrower is not found.
	 */
	public BorrowedBookDetails borrowBook(Long borrowerId, Long bookId) throws BorrowerNotFoundException {
		LOGGER.info("Borrowing book with ID {} for borrower with ID {}", bookId, borrowerId);

		Optional<Book> optionalBook = bookRepository.findById(bookId);
		Optional<Borrower> optionalBorrower = borrowerRepository.findById(borrowerId);

		if (optionalBook.isPresent() && optionalBorrower.isPresent()) {
			Book book = optionalBook.get();
			Borrower borrower = optionalBorrower.get();

			if (book.getStatus() == BookStatus.AVAILABLE) {
				book.setStatus(BookStatus.BORROWED);
				book.setNoOfCopies(book.getNoOfCopies() - 1);

				LOGGER.info("Setting status of book {} to BORROWED", bookId);
				bookRepository.save(book);
				LOGGER.info("Book {} saved after status update", bookId);
				borrowerRepository.save(borrower);
				LOGGER.info("Borrower {} saved after borrowing book", borrowerId);

				// Return borrower and book details encapsulated in BorrowedBookDetails
				return new BorrowedBookDetails(borrower, book);
			} else {
				String errorMessage = "The book is already borrowed by another member.";
				LOGGER.error(errorMessage);
				throw new BorrowerNotFoundException(errorMessage);
			}
		} else {
			String errorMessage = "Borrower or book not found.";
			LOGGER.error(errorMessage);
			throw new EntityNotFoundException(errorMessage);
		}
	}

	/**
	 * Return a book.
	 * 
	 * @param bookId The ID of the book to return.
	 * @throws BookNotFoundException If the book is not found or not currently
	 *                               borrowed.
	 */
	public Book returnBook(Long bookId) throws BookNotFoundException {
		LOGGER.info("Returning book with ID {}", bookId);

		Optional<Book> optionalBook = bookRepository.findById(bookId);

		if (optionalBook.isPresent()) {
			Book book = optionalBook.get();

			if (book.getStatus() == BookStatus.BORROWED) {
				book.setStatus(BookStatus.AVAILABLE);
				book.setNoOfCopies(book.getNoOfCopies() + 1);
				LOGGER.info("Setting status of book {} to AVAILABLE", bookId);
				return bookRepository.save(book);
			} else {
				String errorMessage = "The book is not currently borrowed.";
				LOGGER.error(errorMessage);
				throw new BookNotFoundException(errorMessage);
			}
		} else {
			String errorMessage = "Book not found.";
			LOGGER.error(errorMessage);
			throw new EntityNotFoundException(errorMessage);
		}
	}

}
