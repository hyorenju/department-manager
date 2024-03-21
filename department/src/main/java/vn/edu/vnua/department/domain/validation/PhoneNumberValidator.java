package vn.edu.vnua.department.domain.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        return validatePhoneNumber(phoneNumber);
    }

    public boolean validatePhoneNumber(String phoneNumber) {
        // Loại bỏ các dấu cách, gạch ngang và dấu cộng nếu có
        phoneNumber = phoneNumber.replaceAll("\\s+|-|\\+", "");

        // Kiểm tra số điện thoại có độ dài từ 10 đến 15 chữ số
        if (phoneNumber.length() < 10 || phoneNumber.length() > 15) {
            return false;
        }

        // Kiểm tra số điện thoại bắt đầu bằng +84 hoặc 84
        if (phoneNumber.startsWith("84")) {
            phoneNumber = phoneNumber.substring(2);
        } else if (phoneNumber.startsWith("+84")) {
            phoneNumber = phoneNumber.substring(3);
        }

        // Kiểm tra số điện thoại có đúng đầu số 03, 05, 07, 08, 09 không
        return phoneNumber.matches("^(0?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}$");
    }
}
