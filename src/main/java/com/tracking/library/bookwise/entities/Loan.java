package com.tracking.library.bookwise.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "loans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id")
    @JsonBackReference(value = "book-loans")  // Matches Book.loans
    private Book book;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user-loans")  // Matches User.loans
    private User user;

    @Column(name = "borrow_date", nullable = false)
    private LocalDate borrowDate;

    private LocalDate returnDate;

    @Column(nullable = false)
    private boolean returned = false;
}
