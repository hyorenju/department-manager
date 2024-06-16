package vn.edu.vnua.department.domain.validation;

import vn.edu.vnua.department.common.Constants;

public class ImportInternValidator {
    public static boolean validateInternName(String name){
        return name.matches(Constants.InternConstant.INTERN_NAME_REGEX);
    }

    public static boolean validateNaturalNum(Byte naturalNum){
        return (naturalNum != null && naturalNum >= 0);
    }
}
