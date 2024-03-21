package vn.edu.vnua.department.domain.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import vn.edu.vnua.department.common.ErrorCodeDefinitions;
import vn.edu.vnua.department.response.BaseResponse;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomExceptionHandler {

    @ResponseStatus(OK)
    @ResponseBody
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public BaseResponse methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return BaseResponse.builder()
                .success(false)
                .error(processFieldErrors(fieldErrors))
                .build();
    }

    @ResponseStatus(OK)
    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public BaseResponse notReadableExceptionHandle(HttpMessageNotReadableException ex) {
        return BaseResponse.builder().success(false).error(
                BaseResponse.Error.builder()
                        .code(ErrorCodeDefinitions.VALIDATION_ERROR)
                        .message(ErrorCodeDefinitions.getErrMsg(ErrorCodeDefinitions.VALIDATION_ERROR))
                        .build()
        ).build();
    }

    @ResponseStatus(OK)
    @ResponseBody
    @ExceptionHandler(value = {Exception.class})
    public BaseResponse methodArgumentNotValidException(Exception ex) {
        BaseResponse response = new BaseResponse();
        response.setFailed(ErrorCodeDefinitions.SERVER_ERROR, ex.getMessage());
        return response;
    }

    @ResponseStatus(OK)
    @ResponseBody
    @ExceptionHandler(value = {AuthenticationException.class})
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public BaseResponse handleAuthenticationException(Exception ex) {
        BaseResponse response = new BaseResponse();
        response.setFailed(ErrorCodeDefinitions.TOKEN_REQUIRED, ex.getMessage());
        return response;
    }

    private BaseResponse.Error processFieldErrors(List<FieldError> fieldErrors) {
        BaseResponse.Error error = BaseResponse.Error.builder()
                .code(ErrorCodeDefinitions.VALIDATION_ERROR)
                .message(ErrorCodeDefinitions.getErrMsg(ErrorCodeDefinitions.VALIDATION_ERROR))
                .build();
        List<BaseResponse.ErrorDetail> errorDetailList = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            BaseResponse.ErrorDetail errorDetail = BaseResponse.ErrorDetail.builder()
                    .id(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build();
            errorDetailList.add(errorDetail);
        }
        error.setErrorDetailList(errorDetailList);
        return error;
    }

}

