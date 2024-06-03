package com.libraryportal.service;

import java.util.List;

import com.libraryportal.entity.Borrower;
import com.libraryportal.exception.BorrowerNotFoundException;
import com.libraryportal.exception.DuplicateRegistrationException;

public interface BorrowerService {

    Borrower registerBorrower(Borrower borrower) throws DuplicateRegistrationException;

    List<Borrower> getAllBorrowers();

    Borrower getBorrowerById(Long id) throws BorrowerNotFoundException;

    Borrower createBorrower(Borrower borrower);

    Borrower updateBorrower(Long id, Borrower newBorrower) throws BorrowerNotFoundException;

    void deleteBorrower(Long id);

}
