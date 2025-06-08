package com.tracking.library.bookwise.servicelayer.interfaces;

import com.tracking.library.bookwise.entities.Loan;

import java.util.List;

public interface ILoanService {
    Loan borrowBook(Long bookId, Long userId);
    Loan returnBook(Long loanId);
    List<Loan> getAllLoans();
    List<Loan> getLoansByUserId(Long userId);
}
