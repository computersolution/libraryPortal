package com.libraryportal.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.libraryportal.entity.Borrower;
import com.libraryportal.exception.BorrowerNotFoundException;
import com.libraryportal.exception.DuplicateRegistrationException;
import com.libraryportal.repository.BorrowerRepository;

import jakarta.transaction.Transactional;

/**
 * Service class providing methods to manage borrower in the library.
 */
@Service
public class BorrowerServiceImpl implements BorrowerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BorrowerService.class);

    private final BorrowerRepository borrowerRepository;

    @Autowired
    public BorrowerServiceImpl(BorrowerRepository borrowerRepository) {
        this.borrowerRepository = borrowerRepository;
    }

    /**
     * Register a new borrower.
     * 
     * @param borrower The borrower to register.
     * @return The registered borrower.
     * @throws DuplicateRegistrationException If a borrower with the same email already exists.
     */
    @Transactional
    public Borrower registerBorrower(Borrower borrower) throws DuplicateRegistrationException {
        LOGGER.info("Registering a new borrower: {}", borrower.getEmail());
        validateBorrower(borrower);

        // Check if a borrower with the same email already exists
        Borrower existingBorrower = borrowerRepository.findByEmail(borrower.getEmail());
        if (existingBorrower != null) {
            LOGGER.error("A borrower with the same email already exists: {}", borrower.getEmail());
            throw new DuplicateRegistrationException("A borrower with the same email already exists.");
        }
        // Save the borrower to the database
        Borrower savedBorrower = borrowerRepository.save(borrower);
        LOGGER.info("Borrower registered successfully: {}", savedBorrower.getId());
        return savedBorrower;
    }

    /**
     * Get all borrowers.
     * 
     * @return List of all borrowers.
     */
    public List<Borrower> getAllBorrowers() {
        LOGGER.info("Fetching all borrowers from the database");
        return borrowerRepository.findAll();
    }

    /**
     * Get a borrower by ID.
     * 
     * @param id The ID of the borrower to retrieve.
     * @return The borrower with the specified ID.
     * @throws BorrowerNotFoundException If no borrower is found with the given ID.
     */
    public Borrower getBorrowerById(Long id) throws BorrowerNotFoundException {
        LOGGER.info("Fetching borrower by ID: {}", id);
        return borrowerRepository.findById(id)
                .orElseThrow(() -> new BorrowerNotFoundException("Borrower not found with id: " + id));
    }

    /**
     * Create a new borrower.
     * 
     * @param borrower The borrower to create.
     * @return The created borrower.
     */
    public Borrower createBorrower(Borrower borrower) {
        LOGGER.info("Creating a new borrower: {}", borrower.getEmail());
        return borrowerRepository.save(borrower);
    }

    /**
     * Update an existing borrower.
     * 
     * @param id          The ID of the borrower to update.
     * @param newBorrower The new borrower information.
     * @return The updated borrower.
     * @throws BorrowerNotFoundException If no borrower is found with the given ID.
     */
    public Borrower updateBorrower(Long id, Borrower newBorrower) throws BorrowerNotFoundException {
        LOGGER.info("Updating borrower with ID: {}", id);
        return borrowerRepository.findById(id).map(borrower -> {
            borrower.setName(newBorrower.getName());
            borrower.setEmail(newBorrower.getEmail());
            return borrowerRepository.save(borrower);
        }).orElseThrow(() -> new BorrowerNotFoundException("Borrower not found with id: " + id));
    }

    /**
     * Delete a borrower by ID.
     * 
     * @param id The ID of the borrower to delete.
     */
    public void deleteBorrower(Long id) {
        LOGGER.info("Deleting borrower with ID: {}", id);
        borrowerRepository.deleteById(id);
    }

    /**
     * Validate the borrower before registration.
     * 
     * @param borrower The borrower to validate.
     * @throws DuplicateRegistrationException If the borrower is not valid for registration.
     */
    private void validateBorrower(Borrower borrower) throws DuplicateRegistrationException {
        LOGGER.info("Validating borrower for registration: {}", borrower.getEmail());
        // Perform validation logic here
        if (borrower.getEmail() == null || borrower.getEmail().isEmpty()) {
            LOGGER.error("Email address is required for borrower registration");
            throw new DuplicateRegistrationException("Email address is required.");
        }
    }
}
