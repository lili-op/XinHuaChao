package com.util;

import net.sf.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

/**
 * 通过header头传递access_token获取数据
 * */

public class JieKouUtil {

    //json格式
    public static JSONObject httpDoPost(String serverURL,JSONObject parm, Map<String, String> header) throws IOException {
        StringBuffer sbf = new StringBuffer();
        String strRead = null;
        URL url = new URL(serverURL);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");//请求post方式
        connection.setDoInput(true);
        connection.setDoOutput(true);
        //header内的的参数在这里set。||connection.setRequestProperty("健, "值");
        connection.setRequestProperty("Content-Type", "application/json");
        if (header != null) {
            Iterator it = header.entrySet().iterator();

            while(it.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry)it.next();
                connection.setRequestProperty((String)entry.getKey(), (String)entry.getValue());
            }
        }
        connection.connect();
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(),"UTF-8");
        writer.write(parm.toString());
        writer.flush();
        InputStream is = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        while ((strRead = reader.readLine()) != null) {
            sbf.append(strRead);
        }
        reader.close();
        connection.disconnect();
        String results = sbf.toString();
        JSONObject jsonObject = JSONObject.fromObject(results);
        return jsonObject;
    }
    //json数组格式
    public static JSONObject httpDoPostArr(String serverURL,String access_token,String parm) throws IOException {
        StringBuffer sbf = new StringBuffer();
        String strRead = null;
        URL url = new URL(serverURL);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");//请求post方式
        connection.setDoInput(true);
        connection.setDoOutput(true);
        //header内的的参数在这里set。||connection.setRequestProperty("健, "值");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", "Bearer "+access_token);
        connection.connect();
        OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(),"UTF-8");
        writer.write(parm);
        writer.flush();
        InputStream is = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        while ((strRead = reader.readLine()) != null) {
            sbf.append(strRead);
        }
        reader.close();
        connection.disconnect();
        String results = sbf.toString();
        JSONObject jsonObject = JSONObject.fromObject(results);
        return jsonObject;
    }




}
