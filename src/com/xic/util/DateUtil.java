package com.xic.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

//时间工具类
public class DateUtil {

    //计算两个string类型封装的日期的相差天数
    public int CalculateDays(String ksrq,String jsrq){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        int intValue=0;
        try {
            Date ksrqdate = dateFormat.parse(ksrq);
            Date jsrqdate = dateFormat.parse(jsrq); // 将字符串转换为Date对象

            long diffInMillies = Math.abs(jsrqdate.getTime() - ksrqdate.getTime());
            long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            // 进行强制转换
             intValue = (int) diffInDays;
            System.out.println("两个日期之间的天数差为: " + intValue + " 天");

        } catch (Exception e) {
            System.out.println("日期格式不匹配");
        }

        return intValue;
    }

    public String getByDate() {
        Date date = new Date();
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //System.out.println(format.format(date));

        //方法2：
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");//定义新的日期格式
        String dqrq = formatter.format(date);

        return dqrq;
    }

}
