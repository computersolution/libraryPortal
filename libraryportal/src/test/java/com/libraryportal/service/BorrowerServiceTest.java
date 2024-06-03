package com.libraryportal.service;

import com.libraryportal.entity.Borrower;
import com.libraryportal.exception.BorrowerNotFoundException;
import com.libraryportal.exception.DuplicateRegistrationException;
import com.libraryportal.repository.BorrowerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BorrowerServiceTest {
	
	private static final String BORROWER_NAME="Test Borrower";
	private static final String BORROWER_MAIL_ID="borrower@libraryportal.com";
	private static final String UPDTD_BORROWER_NAME="Test Updated Borrower";
	private static final String UPDTD_BORROWER_MAIL_ID="borrower.updated@libraryportal.com";

	@Mock
	private BorrowerRepository borrowerRepository;

	@InjectMocks
	private BorrowerServiceImpl borrowerService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testRegisterBorrower_Success() throws DuplicateRegistrationException {
		Borrower borrower = new Borrower();
		borrower.setName(BORROWER_NAME);
		borrower.setEmail(BORROWER_MAIL_ID);

		when(borrowerRepository.findByEmail(any())).thenReturn(null);
		when(borrowerRepository.save(any())).thenReturn(borrower);

		Borrower registeredBorrower = borrowerService.registerBorrower(borrower);

		assertNotNull(registeredBorrower);
		assertEquals(BORROWER_NAME, registeredBorrower.getName());
		assertEquals(BORROWER_MAIL_ID, registeredBorrower.getEmail());
	}

	@Test
	void testRegisterBorrower_DuplicateEmail() {
		Borrower borrower = new Borrower();
		borrower.setName(BORROWER_NAME);
		borrower.setEmail(BORROWER_MAIL_ID);

		when(borrowerRepository.findByEmail(BORROWER_MAIL_ID)).thenReturn(borrower);

		assertThrows(DuplicateRegistrationException.class, () -> borrowerService.registerBorrower(borrower));
	}

	@Test
	void testGetAllBorrowers() {
		List<Borrower> borrowers = new ArrayList<>();
		borrowers.add(new Borrower());
		borrowers.add(new Borrower());

		when(borrowerRepository.findAll()).thenReturn(borrowers);

		List<Borrower> result = borrowerService.getAllBorrowers();

		assertNotNull(result);
		assertEquals(2, result.size());
	}

	@Test
	void testGetBorrowerById_Success() throws BorrowerNotFoundException {
		Long borrowerId = 1L;
		Borrower borrower = new Borrower();
		borrower.setId(borrowerId);
		borrower.setName(BORROWER_NAME);
		borrower.setEmail(BORROWER_MAIL_ID);

		when(borrowerRepository.findById(borrowerId)).thenReturn(Optional.of(borrower));

		Borrower result = borrowerService.getBorrowerById(borrowerId);

		assertNotNull(result);
		assertEquals(borrowerId, result.getId());
		assertEquals(BORROWER_NAME, result.getName());
		assertEquals(BORROWER_MAIL_ID, result.getEmail());
	}

	@Test
	void testGetBorrowerById_BorrowerNotFound() {
		Long borrowerId = 1L;
		when(borrowerRepository.findById(borrowerId)).thenReturn(Optional.empty());
		assertThrows(BorrowerNotFoundException.class, () -> borrowerService.getBorrowerById(borrowerId));
	}

	@Test
	void testCreateBorrower() {
		Borrower borrower = new Borrower();
		borrower.setName(BORROWER_NAME);
		borrower.setEmail(BORROWER_MAIL_ID);

		when(borrowerRepository.save(borrower)).thenReturn(borrower);

		Borrower createdBorrower = borrowerService.createBorrower(borrower);

		assertNotNull(createdBorrower);
		assertEquals(BORROWER_NAME, createdBorrower.getName());
		assertEquals(BORROWER_MAIL_ID, createdBorrower.getEmail());
	}

	@Test
	void testUpdateBorrower_Success() throws BorrowerNotFoundException {
		Long borrowerId = 1L;
		Borrower existingBorrower = new Borrower();
		existingBorrower.setId(borrowerId);
		existingBorrower.setName(BORROWER_NAME);
		existingBorrower.setEmail(BORROWER_MAIL_ID);

		Borrower updatedBorrowerData = new Borrower();
		updatedBorrowerData.setName(UPDTD_BORROWER_NAME);
		updatedBorrowerData.setEmail(UPDTD_BORROWER_MAIL_ID);

		when(borrowerRepository.findById(borrowerId)).thenReturn(Optional.of(existingBorrower));
		when(borrowerRepository.save(existingBorrower)).thenReturn(existingBorrower);

		Borrower updatedBorrower = borrowerService.updateBorrower(borrowerId, updatedBorrowerData);

		assertNotNull(updatedBorrower);
		assertEquals(borrowerId, updatedBorrower.getId());
		assertEquals(UPDTD_BORROWER_NAME, updatedBorrower.getName());
		assertEquals(UPDTD_BORROWER_MAIL_ID, updatedBorrower.getEmail());
	}

	@Test
	void testUpdateBorrower_BorrowerNotFound() {
		Long borrowerId = 1L;
		Borrower updatedBorrowerData = new Borrower();
		updatedBorrowerData.setName(UPDTD_BORROWER_NAME);
		updatedBorrowerData.setEmail(UPDTD_BORROWER_MAIL_ID);

		when(borrowerRepository.findById(borrowerId)).thenReturn(Optional.empty());

		assertThrows(BorrowerNotFoundException.class,
				() -> borrowerService.updateBorrower(borrowerId, updatedBorrowerData));
	}

	@Test
	void testDeleteBorrower() {
		Long borrowerId = 1L;
		borrowerService.deleteBorrower(borrowerId);
		verify(borrowerRepository, times(1)).deleteById(borrowerId);
	}
}
