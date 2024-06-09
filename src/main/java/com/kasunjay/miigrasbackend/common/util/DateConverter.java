package com.kasunjay.miigrasbackend.common.util;

import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Slf4j
public class DateConverter {
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public static Date stringToDate(String dateToConvert) {
        Date convertedDate = null;
        String dateFormat = DATE_FORMAT;
        try {
            convertedDate = new SimpleDateFormat(dateFormat).parse(dateToConvert);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }

    public static String convertDateToString(String date) {
        LocalDate givenDate = LocalDate.parse(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        String dateToString = givenDate.format(formatter);
        return (dateToString);
    }

    public static String convertDateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = sdf.format(date);
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateFormat outputFormat = new SimpleDateFormat(DATE_FORMAT);

        try {
            Date date2 = inputFormat.parse(dateString);
            return outputFormat.format(date2);
        } catch (ParseException e) {
            log.error("Exception occurred while converting date to string");
            return null;
        }
    }
}
