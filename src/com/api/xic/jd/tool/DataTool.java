package com.api.xic.jd.tool;




import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataTool {
     //获取当天时间
     public String getNowTime(String dateformat){
         Date now = new Date();
         SimpleDateFormat dateFormat = new SimpleDateFormat(dateformat);
         String hehe = dateFormat.format(now);
         return hehe;
     }
     //获取两个日期之间的所有日期
     public List<LocalDate> getDatesBetween(LocalDate startDate, LocalDate endDate) {
         List<LocalDate> dates = new ArrayList<>();
         long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);
         for (int i = 0; i <= numOfDaysBetween; i++) {
             LocalDate date = startDate.plusDays(i);
             dates.add(date);
         }
         return dates;
     }

}
