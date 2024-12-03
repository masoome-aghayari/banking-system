package ir.azkivaam.banking_system.domain.entity;

/*
 * @author masoome.aghayari
 * @since 11/28/24
 */

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PERSON")
@Getter
@Setter
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, unique = true)
    private String nationalId;

    private String sureName;
    private String lastname;
    private String birthDate;
}
