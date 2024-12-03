package ir.azkivaam.banking_system.validator;

import ir.azkivaam.banking_system.domain.annotation.AmountRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AmountRangeValidator implements ConstraintValidator<AmountRange, Long> {

    private long min;
    private long max;

    @Override
    public void initialize(AmountRange constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value >= min && value <= max;
    }
}
