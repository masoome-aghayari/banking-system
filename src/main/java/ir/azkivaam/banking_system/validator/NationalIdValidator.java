package ir.azkivaam.banking_system.validator;

/*
 * @author masoome.aghayari
 * @since 1/5/24
 */

import ir.azkivaam.banking_system.domain.annotation.NationalId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.stream.IntStream;

public class NationalIdValidator implements ConstraintValidator<NationalId, String> {

    @Override
    public boolean isValid(String nationalId, ConstraintValidatorContext context) {
        return hasInValidLengthOrDigits(nationalId) && hasValidFormat(nationalId);
    }

    public boolean hasValidFormat(String nationalId) {
        try {
            int length = nationalId.length();
            nationalId = length == 8 ? "00" + nationalId :
                         length == 9 ? "0" + nationalId : nationalId;
            int lastDigit = calculateNationalCodeLastDigit(nationalId);
            int nationalCodeLastDigit = nationalId.charAt(9) - 48;
            return nationalCodeLastDigit == lastDigit;
        } catch (StringIndexOutOfBoundsException | NullPointerException e) {
            return false;
        }
    }

    public boolean hasInValidLengthOrDigits(String nationalId) {
        return (nationalId.matches("[0-9]{8,10}") ||
                (nationalId.matches("0{8,10}|1{8,10}|2{8,10}|3{8,10}|4{8,10}|5{8,10}|6{8,10}|7{8,10}|8{8,10}|9{8,10}")));
    }

    public int calculateNationalCodeLastDigit(String nationalId) throws StringIndexOutOfBoundsException {
        int sum = IntStream.range(0, 9)
                           .map(i -> Character.getNumericValue(nationalId.charAt(i)) * (10 - i))
                           .sum();
        int remainder = sum % 11;
        return remainder < 2 ? remainder : 11 - remainder;
    }
}
