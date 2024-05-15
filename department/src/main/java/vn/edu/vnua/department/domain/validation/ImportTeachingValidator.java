package vn.edu.vnua.department.domain.validation;

public class ImportTeachingValidator {
    public static boolean validateNaturalNum(Integer naturalNum){
        return (naturalNum != null && naturalNum >= 0);
    }

    public static boolean validateNaturalNum(Byte naturalNum){
        return (naturalNum != null && naturalNum >= 0);
    }
}
