package com.libraryportal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.libraryportal.entity.Borrower;

/**
 * Repository interface for accessing borrower data in the database.
 */
public interface BorrowerRepository extends JpaRepository<Borrower, Long> {

    /**
     * Find a borrower by email address.
     * 
     * @param email The email address of the borrower to find.
     * @return The borrower with the specified email address, or null if not found.
     */
    Borrower findByEmail(String email);
}
