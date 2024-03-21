package vn.edu.vnua.department.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseListItemResponse<T> extends BaseResponse {
    private DataList<T> data;

    @Data
    public static class DataList<T> {
        private long total = 0;
        private List<T> items;
    }

    public void setResult(List<T> items, long total) {
        data = new DataList<>();
        data.setItems(items);
        data.setTotal(total);
    }
}
