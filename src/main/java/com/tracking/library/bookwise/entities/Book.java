package com.tracking.library.bookwise.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String author;

    @Column(unique = true)
    private String isbn;

    @Column(nullable = false)
    private boolean available = true;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "book-loans")  // Match with Loan.book
    private List<Loan> loans = new ArrayList<>();
}

