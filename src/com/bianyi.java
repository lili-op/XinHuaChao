package com;

import com.api.xic.jd.util.JieKouJdjcsj;
import com.util.JingDieRenZheng;
import net.sf.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class bianyi {
    public static void main(String[] args) throws Exception {
        long timestamp = System.currentTimeMillis() / 1000;
        System.out.println(timestamp);

//        Date date = new Date();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date date2 = sdf.parse("2024-02-29");
//        //先判断应收款日期到当前日期有多少天
//        long diff = date.getTime() - date2.getTime();
//        long days = diff / (1000 * 60 * 60 * 24);
//        System.out.println("两个日期之间的天数：" + days);
        // 获取当前日期
//        LocalDate currentDate = LocalDate.now();
//
//        // 创建日期格式化对象
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
//
//        // 使用格式化对象将LocalDate转换为字符串
//        String dateString = currentDate.format(formatter);
//
//        // 输出结果
//        System.out.println(dateString);


//        long timestamp = System.currentTimeMillis() / 1000;
//
//        JSONObject json= new JSONObject();
//        json.put("code","1710152903e7d02bbdec8afbcefbdbe6");
//        JSONObject data = JingDieRenZheng.gettoken(json,timestamp);//获取到token和sessiontoken和auth_data
//        System.out.println("基础"+data);
//        TimeUnit.SECONDS.sleep(10);
//        JSONObject data1 = JingDieRenZheng.gengxauthdata(data,timestamp);
//        System.out.println("更新1"+data1);
//        TimeUnit.SECONDS.sleep(10);
//        JSONObject data2 = JingDieRenZheng.gengxauthdata(data1,timestamp);
//        System.out.println("更新2"+data2);
//        TimeUnit.SECONDS.sleep(10);
//        data = JingDieRenZheng.gengxauthdata(data,timestamp);
//        System.out.println("更新2"+data);
//
//        data = JingDieRenZheng.gengxauthdata(data,timestamp);
//        System.out.println("更新3"+data);
//        data = JingDieRenZheng.gengxauthdata(data,timestamp);
//        System.out.println("更新4"+data);
        int i = 1;
    }
}
