package com.polezhaiev.avtodiva.service.schedule;

import java.time.LocalDate;

public class WorkingHoursProvider {
    public static int[][] getWorkingHours(String instructorName, LocalDate date) {
        int[][] fullDay = {
                {7, 0}, {10, 30}, {14, 0}
        };
//        int[][] afternoon = {
//                {14, 0}, {17, 15}
//        };
//        int[][] upWork = {
//                {7, 0}, {10, 30}, {14, 0}, {17, 15}
//        };
//
//        if ("Юлія".equalsIgnoreCase(instructorName)) {
//            return afternoon;
//        }
//
//        if ("Діна".equalsIgnoreCase(instructorName)) {
//            if (date.getDayOfWeek() == DayOfWeek.MONDAY) {
//                return upWork;
//            }
//            else if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
//                return new int[][] {{15, 0}};
//            }
//            else {
//                return afternoon;
//            }
//        }

        return fullDay;
    }
}
