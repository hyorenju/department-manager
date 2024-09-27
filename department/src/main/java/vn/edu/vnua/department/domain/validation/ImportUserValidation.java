package vn.edu.vnua.department.domain.validation;

import vn.edu.vnua.department.common.Constants;

public class ImportUserValidation {
    public static boolean validateUserId(String id){
        return id.matches(Constants.UserConstant.ID_REGEX);
    }

    public static boolean validateUserName(String name){
        return name.matches(Constants.UserConstant.NAME_REGEX);
    }

    public static boolean validateUserEmail(String email){
        return email.matches(Constants.UserConstant.EMAIL_REGEX);
    }

    public static boolean validateUserPhoneNumber(String phoneNumber){
        return phoneNumber.matches(Constants.UserConstant.PHONE_NUMBER_REGEX);
    }
}
