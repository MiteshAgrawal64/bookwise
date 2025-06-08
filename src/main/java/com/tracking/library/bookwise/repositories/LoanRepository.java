package com.tracking.library.bookwise.repositories;

import com.tracking.library.bookwise.entities.Loan;
import com.tracking.library.bookwise.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUser(User user);
    List<Loan> findByReturnedFalse();
}
