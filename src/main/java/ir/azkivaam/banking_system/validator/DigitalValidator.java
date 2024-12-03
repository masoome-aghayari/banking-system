package ir.azkivaam.banking_system.validator;


import ir.azkivaam.banking_system.domain.annotation.Digital;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

/**
 * @author masoome.aghayari
 * @since 1/5/24
 */

public class DigitalValidator implements ConstraintValidator<Digital, String> {
    private int digitsCount;

    @Override
    public void initialize(Digital constraintAnnotation) {
        digitsCount = constraintAnnotation.digitsCount();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return StringUtils.isNumeric(value) && value.length() == digitsCount;
    }
}
