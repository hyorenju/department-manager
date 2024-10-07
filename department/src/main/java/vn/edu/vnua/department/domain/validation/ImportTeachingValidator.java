package vn.edu.vnua.department.domain.validation;

import java.sql.Timestamp;

public class ImportTeachingValidator {
    public static boolean validateNaturalNum(Integer naturalNum){
        return (naturalNum != null && naturalNum >= 0);
    }

    public static boolean validateNaturalNum(Byte naturalNum){
        return (naturalNum != null && naturalNum >= 0);
    }

    public static boolean validateDeadline(Timestamp deadline) {
        return !deadline.equals(new Timestamp(0));
    }
}
