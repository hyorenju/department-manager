package vn.edu.vnua.department.service.excel.internship;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import vn.edu.vnua.department.common.Constants;

import java.util.concurrent.Callable;

@Data
@AllArgsConstructor
public class ReadInternshipWorker implements Callable<String> { private Row row;

    @Override
    public String call() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            Cell cell = row.getCell(i);
            String data = "";
            if (cell != null) {
                cell.setCellType(CellType.STRING);
                data = cell.getStringCellValue();
            }
            stringBuilder.append(data);
            stringBuilder.append(Constants.AppendCharacterConstant.APPEND_CHARACTER);
        }
        return stringBuilder + "end";
    }
}
