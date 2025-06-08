package com.tracking.library.bookwise.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tracking.library.bookwise.entities.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = true)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "user-loans")  // Match with Loan.user
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Loan> loans = new ArrayList<>();
}
