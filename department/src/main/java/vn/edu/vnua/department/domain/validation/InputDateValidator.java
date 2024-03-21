package vn.edu.vnua.department.domain.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vn.edu.vnua.department.common.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class InputDateValidator implements ConstraintValidator<InputDate, String> {
    @Override
    public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {
        if (date == null)
            return false;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DateTimeConstants.DATE_FORMAT);
        simpleDateFormat.setLenient(false);
        try {
            return simpleDateFormat.parse(date) != null;
        } catch (ParseException e) {
            return false;
        }
    }
}
