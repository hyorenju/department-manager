package vn.edu.vnua.department.util;

import vn.edu.vnua.department.common.Constants;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;

public class MyUtils {
    public static Timestamp convertTimestampFromString(String inputDate) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DateTimeConstants.DATE_FORMAT);
        simpleDateFormat.setLenient(false);
        return new Timestamp(simpleDateFormat.parse(inputDate).getTime());
    }

    public static Timestamp convertTimestampFromExcel(String inputDate) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DateTimeConstants.DATE_FORMAT);
            simpleDateFormat.setLenient(false);
            return new Timestamp(simpleDateFormat.parse(inputDate).getTime());
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

    public Timestamp test(long inputDate) throws ParseException {
        return new Timestamp(inputDate);
    }
}
