package ir.azkivaam.banking_system.domain.entity;

/*
 * @author masoome.aghayari
 * @since 11/28/24
 */

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "BRANCH")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 20)
    private String name;

    @Column(unique = true, nullable = false, length = 6)
    private String code;
}
