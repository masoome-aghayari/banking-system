package ir.azkivaam.banking_system.domain.dto;

import ir.azkivaam.banking_system.domain.annotation.Digital;
import ir.azkivaam.banking_system.domain.annotation.NationalId;
import ir.azkivaam.banking_system.domain.entity.Person;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for {@link Person}
 */
@Getter
@Setter
@Builder
public class PersonDto {
    private Long id;

    @NationalId
    private String nationalId;

    @NotBlank(message = "SureName can not be blank")
    private String sureName;

    @NotBlank(message = "LastName can not be blank")
    private String lastname;

    @Digital(digitsCount = 4, message = "Enter a 4 digits birth year")
    private String birthDate;
}