package vn.edu.vnua.department.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasePageItemResponse<T> extends BaseResponse{
    private DataPage<T> data;

    @Data
    public static class DataPage<T> {
        private long page = 0;
        private long size = 0;
        private long total = 0;
        private List<T> items;
    }

    public void setPageItems(long page, long size, long total, List<T> items) {
        data = new DataPage<T>();
        data.setPage(page);
        data.setSize(size);
        data.setTotal(total);
        data.setItems(items);


    }
}
