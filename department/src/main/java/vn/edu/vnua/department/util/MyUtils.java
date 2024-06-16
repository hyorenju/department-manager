package vn.edu.vnua.department.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import vn.edu.vnua.department.common.Constants;
import vn.edu.vnua.department.teaching.model.TeachingExcelData;
import vn.edu.vnua.department.teaching.request.CreateTeachingRequest;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyUtils {
    public static Timestamp convertTimestampFromString(String inputDate) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DateTimeConstants.DATE_FORMAT);
        simpleDateFormat.setLenient(false);
        return new Timestamp(simpleDateFormat.parse(inputDate).getTime());
    }

    public static Timestamp convertTimestampFromExcel(String inputDate) {
        try {
            if(inputDate.contains("/")) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DateTimeConstants.DATE_FORMAT);
                simpleDateFormat.setLenient(false);
                return new Timestamp(simpleDateFormat.parse(inputDate).getTime());
            } else {
                // Ngày bắt đầu là 1/1/1900
                LocalDateTime startDate = LocalDateTime.of(1900, 1, 1, 0, 0);

                Integer numberOfDays = parseIntegerFromString(inputDate);

                // Tính toán ngày kết thúc bằng cách thêm số ngày vào ngày bắt đầu
                LocalDateTime endDate = startDate.plusDays(numberOfDays - 2);

                return Timestamp.valueOf(endDate);
            }
        } catch (ParseException e) {
            return new Timestamp(0);
        }
    }

    public static String formatDobToPassword(String inputDob) {
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d/M/yyyy");
            LocalDate date = LocalDate.parse(inputDob, inputFormatter);

            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return date.format(outputFormatter);
        } catch (DateTimeParseException e) {
            return ("00/00/0000");
        }
    }

    public static String convertTimestampToString(Timestamp inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(inputDate);
    }

    public static String parseFloatToString(Float input) {
        try {
            return String.format("%.2f", input);
        } catch (NumberFormatException e) {
            return "";
        }
    }

    public static Float parseFloatFromString(String input) {
        try {
            return Float.parseFloat(input);
        } catch (NumberFormatException e) {
            return (float) -1;
        }
    }

    public static Integer parseIntegerFromString(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static Byte parseByteFromString(String input) {
        try {
            return Byte.parseByte(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void test(String inputDate) throws ParseException, IOException {
        Timestamp currentDate = convertTimestampFromString(inputDate);
        int month = currentDate.getMonth() + 1;
        int year = currentDate.getYear() + 1900;
        int term = month > 6 ? 1 : 2;
        String schoolYear = month > 6 ? (year+"-"+(year+1)) : ((year-1)+"-"+year);
        System.out.println(term + "\n" + schoolYear);
    }
}
