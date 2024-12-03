package ir.azkivaam.banking_system.domain.annotation;

import ir.azkivaam.banking_system.validator.DigitalValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;

import java.lang.annotation.*;

/*
 * @author masoome.aghayari
 * @since 1/5/24
 */
@Documented
@Constraint(validatedBy = DigitalValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ReportAsSingleViolation
public @interface Digital {
    String message() default "just digits are allowed";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int digitsCount() default 10;

}
