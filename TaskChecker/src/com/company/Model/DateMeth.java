package com.company.Model;

import java.text.SimpleDateFormat;
import java.util.Date;

//этот класс предназначен для работы с датами.

public class DateMeth {

    public DateMeth() {
    }

    private static Date currentDate = new Date();

    //получение текущей даты
    private static String getCurrentDate() {
        SimpleDateFormat format1 = new SimpleDateFormat("dd.MM.yyyy");

        String s = format1.format(currentDate);

        return s;
    }

    // ПРоверка сделано ли задание: выделяются год месяц день и сравниваются
    public static String isDone(String date) {

        //дата переводится в формате дд,мм,гггг. Далее мы эту дату расчленяем на значения и сравниваем
        //  текущую дату и дату в задании. Сначала сравнивается год, потом месяц, потом день

        String message = "";

//текущая дата в формате String
        String currDate = getCurrentDate();

        int year = Integer.parseInt(date.split("\\.")[2]);
        int month = Integer.parseInt(date.split("\\.")[1]);
        int dayOfMonth = Integer.parseInt(date.split("\\.")[0]);
        int size = dayOfMonth + (month - 1) * 30 + year * 365;

        int currYear = Integer.parseInt(currDate.split("\\.")[2]);
        int currmonth = Integer.parseInt(currDate.split("\\.")[1]);
        int currdayOfMonth = Integer.parseInt(currDate.split("\\.")[0]);
        int currSize = currdayOfMonth + (currmonth - 1) * 30 + currYear * 365;


        if (size < currSize) {
            message = "Потрачено";
        } else {
            message = "В процессе";
        }
        return message;
    }

    //проверка корректного ввода года
    public static int checkYear(String yearStr) {
        int year;

        try {
            year = Integer.parseInt(yearStr);

            if (year < 0)
                year = 1;
        } catch (NumberFormatException ex) {
            year = 1;
        }
        return year;
    }

    //проверка корректного ввода месяца
    public static int checkMonth(String monthStr) {
        int month;

        try {
            month = Integer.parseInt(monthStr);

            if (month < 0 || month > 13)
                month = 1;
        } catch (NumberFormatException ex) {
            month = 1;
        }
        return month;
    }

    // проверка корректного ввода дня. Ввод месяца для проверки корректного ввода дня.
    public static int checkDay(String dayStr, String monthStr) {
        int month = checkMonth(monthStr);
        int day;
        int dayNumber;

        try {

            switch (month) {
                case 1:
                    dayNumber = 31;
                    break;
                case 2:
                    dayNumber = 28;
                    break;
                case 3:
                    dayNumber = 31;
                    break;
                case 4:
                    dayNumber = 30;
                    break;
                case 5:
                    dayNumber = 31;
                    break;
                case 6:
                    dayNumber = 30;
                    break;
                case 7:
                    dayNumber = 31;
                    break;
                case 8:
                    dayNumber = 31;
                    break;
                case 9:
                    dayNumber = 30;
                    break;
                case 10:
                    dayNumber = 31;
                    break;
                case 11:
                    dayNumber = 30;
                    break;
                case 12:
                    dayNumber = 31;
                    break;
                default:
                    dayNumber = 31;
                    break;
            }


            day = Integer.parseInt(dayStr);


            if (day < 1 || day > dayNumber) {
                day = 1;
            }


        } catch (NumberFormatException ex) {
            day = 1;
        }
        return day;
    }
}
