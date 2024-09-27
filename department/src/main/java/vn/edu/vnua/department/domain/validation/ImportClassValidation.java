package vn.edu.vnua.department.domain.validation;

import vn.edu.vnua.department.common.Constants;

public class ImportClassValidation {
    public static boolean validateClassId(String id){
        return id.matches(Constants.ClassConstant.ID_REGEX);
    }
}
