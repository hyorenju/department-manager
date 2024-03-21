package vn.edu.vnua.department.controller;

import org.springframework.http.ResponseEntity;
import vn.edu.vnua.department.response.BaseItemResponse;
import vn.edu.vnua.department.response.BaseListItemResponse;
import vn.edu.vnua.department.response.BasePageItemResponse;

import java.util.List;

public class BaseController {

    protected <T> ResponseEntity<?> buildItemResponse(T data) {
        BaseItemResponse<T> response = new BaseItemResponse<>();
        response.setData(data);
        response.setSuccess(true);
        return ResponseEntity.ok(response);
    }

    protected <T> ResponseEntity<?> buildListItemResponse(List<T> data, long total) {
        BaseListItemResponse<T> response = new BaseListItemResponse<>();
        response.setResult(data, total);
        response.setSuccess(true);
        return ResponseEntity.ok(response);
    }

    protected <T> ResponseEntity<?> buildPageItemResponse(long page, long limit, long totalElements, List<T> items){
        BasePageItemResponse<T> response = new BasePageItemResponse<>();
        response.setPageItems(page, limit, totalElements, items);
        response.setSuccess(true);
        return ResponseEntity.ok(response);
    }
}
