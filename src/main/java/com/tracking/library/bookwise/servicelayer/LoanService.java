package com.tracking.library.bookwise.servicelayer;

import com.tracking.library.bookwise.entities.Book;
import com.tracking.library.bookwise.entities.Loan;
import com.tracking.library.bookwise.entities.User;
import com.tracking.library.bookwise.repositories.BookRepository;
import com.tracking.library.bookwise.repositories.LoanRepository;
import com.tracking.library.bookwise.repositories.UserRepository;
import com.tracking.library.bookwise.servicelayer.interfaces.ILoanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService implements ILoanService {
    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(LoanService.class);

    @Override
    public Loan borrowBook(Long bookId, Long userId) {
        logger.info("Attempting to borrow book with ID: {} by user ID: {}", bookId, userId);

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> {
                    logger.error("Book not found with ID: {}", bookId);
                    return new RuntimeException("Book not found");
                });

        if (!book.isAvailable()) {
            logger.warn("Book with ID: {} is already borrowed", bookId);
            throw new RuntimeException("Book is already borrowed");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", userId);
                    return new RuntimeException("User not found");
                });

        book.setAvailable(false);
        bookRepository.save(book);

        Loan loan = Loan.builder()
                .book(book)
                .user(user)
                .borrowDate(LocalDate.now())
                .returned(false)
                .build();

        Loan savedLoan = loanRepository.save(loan);
        logger.info("Book with ID: {} borrowed by user ID: {}, Loan ID: {}", bookId, userId, savedLoan.getId());

        return savedLoan;
    }

    @Override
    public Loan returnBook(Long loanId) {
        logger.info("Attempting to return loan with ID: {}", loanId);

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> {
                    logger.error("Loan not found with ID: {}", loanId);
                    return new RuntimeException("Loan not found");
                });

        if (loan.isReturned()) {
            logger.warn("Loan with ID: {} has already been returned", loanId);
            throw new RuntimeException("Book already returned");
        }

        loan.setReturned(true);
        loan.setReturnDate(LocalDate.now());

        Book book = loan.getBook();
        book.setAvailable(true);
        bookRepository.save(book);

        Loan updatedLoan = loanRepository.save(loan);
        logger.info("Loan with ID: {} marked as returned", loanId);

        return updatedLoan;
    }

    @Override
    public List<Loan> getAllLoans() {
        List<Loan> loans = loanRepository.findAll();
        logger.info("Fetched all loans, total count: {}", loans.size());
        return loans;
    }

    @Override
    public List<Loan> getLoansByUserId(Long userId) {
        logger.info("Fetching loans for user ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", userId);
                    return new RuntimeException("User not found");
                });

        List<Loan> userLoans = loanRepository.findByUser(user);
        logger.info("Found {} loans for user ID: {}", userLoans.size(), userId);
        return userLoans;
    }
}
