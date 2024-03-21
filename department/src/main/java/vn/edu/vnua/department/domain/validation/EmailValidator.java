package vn.edu.vnua.department.domain.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<EmailAnnotation, String> {
    private final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@gmail(.+)$";

    private final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    @Override
    public void initialize(EmailAnnotation constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return validateEmail(email);
    }

    public boolean validateEmail(String email){
        if (email == null) {
            return true; // Allow null values
        }

        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
