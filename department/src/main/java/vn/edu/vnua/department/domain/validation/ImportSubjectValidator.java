package vn.edu.vnua.department.domain.validation;

import vn.edu.vnua.department.common.Constants;

public class ImportSubjectValidator {
    public static boolean validateSubjectId(String id){
        return id.matches(Constants.SubjectConstant.ID_REGEX);
    }
    public static boolean validateSubjectName(String name){
        return name.matches(Constants.SubjectConstant.SUBJECT_NAME_REGEX);
    }
    public static boolean validateSubjectCredits(Integer naturalNum){
        return (naturalNum != null && naturalNum >= 0);
    }
}
