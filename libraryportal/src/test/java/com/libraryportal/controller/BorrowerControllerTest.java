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

import com.libraryportal.entity.Borrower;
import com.libraryportal.exception.BorrowerNotFoundException;
import com.libraryportal.exception.DuplicateRegistrationException;
import com.libraryportal.exception.ErrorResponse;
import com.libraryportal.service.BorrowerServiceImpl;

class BorrowerControllerTest {
	private static final String BORROWER_NAME = "Test Borrower";
	private static final String BORROWER_MAIL_ID = "borrower@libraryportal.com";
	private static final String UPDTD_BORROWER_NAME = "Test Updated Borrower";
	private static final String UPDTD_BORROWER_MAIL_ID = "borrower.updated@libraryportal.com";

	@Mock
	private BorrowerServiceImpl borrowerService;

	@InjectMocks
	private BorrowerController borrowerController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testRegisterBorrower_Success() throws DuplicateRegistrationException {
		Borrower borrower = new Borrower();
		borrower.setName(BORROWER_NAME);
		borrower.setEmail(BORROWER_MAIL_ID);

		when(borrowerService.registerBorrower(any(Borrower.class))).thenReturn(borrower);

		ResponseEntity<?> response = borrowerController.registerBorrower(borrower);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(borrower, response.getBody());
	}

	@Test
	void testRegisterBorrower_DuplicateRegistration() throws DuplicateRegistrationException {
		Borrower borrower = new Borrower();
		borrower.setName(BORROWER_NAME);
		borrower.setEmail(BORROWER_MAIL_ID);

		doThrow(DuplicateRegistrationException.class).when(borrowerService).registerBorrower(any(Borrower.class));

		ResponseEntity<?> response = borrowerController.registerBorrower(borrower);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals(ErrorResponse.class, response.getBody().getClass());
	}

	@Test
	void testGetAllBorrowers_Success() {
		List<Borrower> borrowers = new ArrayList<>();
		borrowers.add(new Borrower());
		borrowers.add(new Borrower());

		when(borrowerService.getAllBorrowers()).thenReturn(borrowers);

		ResponseEntity<List<Borrower>> response = borrowerController.getAllBorrowers();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(borrowers, response.getBody());
	}

	@Test
	void testGetBorrowerById_Success() throws BorrowerNotFoundException {
		Long borrowerId = 1L;
		Borrower borrower = new Borrower();
		borrower.setId(borrowerId);
		borrower.setName(BORROWER_NAME);
		borrower.setEmail(BORROWER_MAIL_ID);

		when(borrowerService.getBorrowerById(borrowerId)).thenReturn(borrower);

		ResponseEntity<?> response = borrowerController.getBorrowerById(borrowerId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(borrower, response.getBody());
	}

	@Test
	void testGetBorrowerById_NotFound() throws BorrowerNotFoundException {
		Long borrowerId = 1L;

		doThrow(BorrowerNotFoundException.class).when(borrowerService).getBorrowerById(borrowerId);

		ResponseEntity<?> response = borrowerController.getBorrowerById(borrowerId);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertTrue(response.getBody() instanceof ErrorResponse);
	}

	@Test
	void testUpdateBorrower_Success() throws BorrowerNotFoundException {
		Long borrowerId = 1L;
		Borrower updatedBorrower = new Borrower();
		updatedBorrower.setId(borrowerId);
		updatedBorrower.setName(UPDTD_BORROWER_NAME);
		updatedBorrower.setEmail(UPDTD_BORROWER_MAIL_ID);

		when(borrowerService.updateBorrower(borrowerId, updatedBorrower)).thenReturn(updatedBorrower);

		ResponseEntity<?> response = borrowerController.updateBorrower(borrowerId, updatedBorrower);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(updatedBorrower, response.getBody());
	}

	@Test
	void testUpdateBorrower_NotFound() throws BorrowerNotFoundException {
		Long borrowerId = 1L;
		Borrower updatedBorrower = new Borrower();
		updatedBorrower.setId(borrowerId);
		updatedBorrower.setName(UPDTD_BORROWER_NAME);
		updatedBorrower.setEmail(UPDTD_BORROWER_MAIL_ID);

		doThrow(BorrowerNotFoundException.class).when(borrowerService).updateBorrower(borrowerId, updatedBorrower);

		ResponseEntity<?> response = borrowerController.updateBorrower(borrowerId, updatedBorrower);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertTrue(response.getBody() instanceof ErrorResponse);
	}

	@Test
	void testDeleteBorrower_Success() {
		Long borrowerId = 1L;

		ResponseEntity<Void> response = borrowerController.deleteBorrower(borrowerId);

		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	@Test
	public void testRegisterBorrowerCatchBlock() {
		try {
			when(borrowerService.registerBorrower(Mockito.any())).thenThrow(new RuntimeException("Test Exception"));
			BorrowerController borrowerController = new BorrowerController(borrowerService);
			ResponseEntity<?> responseEntity = borrowerController.registerBorrower(new Borrower());
			assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
			assertEquals(null, responseEntity.getBody());
		} catch (DuplicateRegistrationException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testRegisterBorrowerDuplicateRegistrationCatchBlock() {
		try {
			when(borrowerService.registerBorrower(Mockito.any()))
					.thenThrow(new DuplicateRegistrationException("Duplicate registration"));
			BorrowerController borrowerController = new BorrowerController(borrowerService);
			ResponseEntity<?> responseEntity = borrowerController.registerBorrower(new Borrower());
			assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
			assertEquals(ErrorResponse.class, responseEntity.getBody().getClass());
		} catch (DuplicateRegistrationException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetAllBorrowersCatchBlock() {
		when(borrowerService.getAllBorrowers()).thenThrow(new RuntimeException("Test Exception"));
		BorrowerController borrowerController = new BorrowerController(borrowerService);
		ResponseEntity<List<Borrower>> responseEntity = borrowerController.getAllBorrowers();
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
		assertEquals(null, responseEntity.getBody());
	}

	@Test
	public void testGetBorrowerByIdCatchBlock() {
		try {
			when(borrowerService.getBorrowerById(anyLong())).thenThrow(new RuntimeException("Test Exception"));
			BorrowerController borrowerController = new BorrowerController(borrowerService);
			ResponseEntity<?> responseEntity = borrowerController.getBorrowerById(1L);
			assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
			assertEquals("Internal server error", responseEntity.getBody());
		} catch (BorrowerNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetBorrowerByIdBorrowerNotFoundExceptionCatchBlock() {
		try {
			when(borrowerService.getBorrowerById(anyLong()))
					.thenThrow(new BorrowerNotFoundException("Borrower not found"));
			BorrowerController borrowerController = new BorrowerController(borrowerService);
			ResponseEntity<?> responseEntity = borrowerController.getBorrowerById(1L);
			assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
			assertEquals(ErrorResponse.class, responseEntity.getBody().getClass());
		} catch (BorrowerNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testUpdateBorrowerCatchBlock() {
		try {
			when(borrowerService.updateBorrower(anyLong(), any())).thenThrow(new RuntimeException("Test Exception"));
			BorrowerController borrowerController = new BorrowerController(borrowerService);
			ResponseEntity<?> responseEntity = borrowerController.updateBorrower(1L, new Borrower());
			assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
			assertEquals(null, responseEntity.getBody());
		} catch (BorrowerNotFoundException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void testUpdateBorrowerBorrowerNotFoundExceptionCatchBlock() {
		try {
			when(borrowerService.updateBorrower(anyLong(), any()))
					.thenThrow(new BorrowerNotFoundException("Borrower not found"));
			BorrowerController borrowerController = new BorrowerController(borrowerService);
			ResponseEntity<?> responseEntity = borrowerController.updateBorrower(1L, new Borrower());
			assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
			assertEquals(ErrorResponse.class, responseEntity.getBody().getClass());
		} catch (BorrowerNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDeleteBorrowerCatchBlock() {
		doThrow(new RuntimeException("Test Exception")).when(borrowerService).deleteBorrower(anyLong());
		BorrowerController borrowerController = new BorrowerController(borrowerService);
		ResponseEntity<Void> responseEntity = borrowerController.deleteBorrower(1L);
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
		assertEquals(null, responseEntity.getBody());
	}

}
