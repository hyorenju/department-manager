package vn.edu.vnua.department.domain.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = InputDateValidator.class)
@Target({ TYPE, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Documented
public @interface InputDate {
    String message() default "Ngày phải là định dạng dd/MM/yyyy";

    Class <?> [] groups() default {};
    Class <? extends Payload> [] payload() default {};
}