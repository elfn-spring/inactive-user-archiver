package com.elfn.inactiveuserarchiver.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @Author: Elimane
 */

@Entity
@Table(name = "archived_users ")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArchivedUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "archived_date ")
    private LocalDate archivedDate;
}
