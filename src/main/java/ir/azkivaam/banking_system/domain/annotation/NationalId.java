package ir.azkivaam.banking_system.domain.annotation;

import ir.azkivaam.banking_system.validator.NationalIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.NotBlank;

import java.lang.annotation.*;

/*
 * @author masoome.aghayari
 * @since 1/5/24
 */
@Documented
@Constraint(validatedBy = NationalIdValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
@NotBlank
public @interface NationalId {
    String message() default "National Id is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
