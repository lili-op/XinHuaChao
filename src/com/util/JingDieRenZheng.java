package com.util;





import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class JingDieRenZheng {

    static String client_id = "270962";
    static String client_secret = "f4950271dc9e061b33de5d4f2ca851e7";
    static String url = "https://api.kingdee.com/koas/oauth2/access_token?";
    static String url2 = "https://api.kingdee.com/koas/user/get_service_gateway?access_token=";
    static String url3 = "https://api.kingdee.com/koas/user/refresh_auth_data?";
    static String pid = "16010091586cab90f3eeb764ceab5eef";//固定
    //static String acctnumber = "UE162828052021072818041314R";//账套id(不同账套不一样)芯华超0
    //static String acctnumber = "UE162828052021072709481828R";//账套id(不同账套不一样)芯富强1
   // static String acctnumber = "UE162828052021111014415144";//账套id(不同账套不一样)芯车新能源2
    static String icrmid = "2c9223b083cc0f130183e4c32be01544";//固定

    public static JSONObject getCoke(JSONObject parm) throws Exception {
        long timestamp = System.currentTimeMillis() / 1000;//循环时间戳

        url += "client_id=" + client_id + "&client_secret=" + client_secret;
        //通过code获取到token和session
        JSONObject jsonObject = httpPosyauthdata(url,  String.valueOf(timestamp), parm);
        JSONObject data = jsonObject.getJSONObject("data");

        String access_token = data.getString("access_token");//全局都要用
        String session_id = data.getString("session_id");
        return data;
    }


    //获取到token和sessiontoken和auth_data和gw_router_addr
    public static JSONObject gettoken(String acctnumber,String session_id,String access_token) throws Exception {

//        JSONObject coke = getCoke();
//        String access_token = coke.getString("access_token");//全局都要用
//        String session_id = coke.getString("session_id");


        String urls =url2 + access_token;
        JSONObject json2 = new JSONObject();
        json2.put("session_id", session_id);
        json2.put("pid", pid);
        json2.put("acctnumber", acctnumber);
        json2.put("icrmid", icrmid);
        long timestamp = System.currentTimeMillis() / 1000;//循环时间戳
        JSONObject de = JingDieRenZheng.httpPosyauthdata(urls, String.valueOf(timestamp), json2);
        JSONObject authData = de.getJSONObject("data");
        String auth_data = authData.getString("auth_data");
        String gw_router_addr = authData.getString("gw_router_addr");
        JSONObject extend_data = authData.getJSONObject("extend_data");
        String refresh_auth_data_token = extend_data.getString("refresh_auth_data_token");

        JSONObject json= new JSONObject();
        json.put("access_token",access_token);
        json.put("auth_data",auth_data);
        json.put("gw_router_addr",gw_router_addr);
        json.put("refresh_auth_data_token",refresh_auth_data_token);

        return json;
    }

    //通用认证调用接口工具类
    public static JSONObject httpPosyauthdata(String serverURL, String shijiancuo, JSONObject parm) throws Exception {
        //headers
        Map<String, String> map = new HashMap<>();
        map.put("KIS-State", "1");
        map.put("KIS-Timestamp", shijiancuo);
        map.put("KIS-TraceID", "270962");
        JSONObject jsonObject = JieKouUtil.httpDoPost(serverURL, parm, map);
        return jsonObject;
    }

    //更新业务接口
    public static JSONObject gengxauthdata(JSONObject parm, long timestamp) throws Exception {
        String refresh_auth_data_token = parm.getString("refresh_auth_data_token");
        String access_token = parm.getString("access_token");
        String gw_router_addr = parm.getString("gw_router_addr");

        url3 += "client_id=" + client_id + "&client_secret=" + client_secret;
        JSONObject json2 = new JSONObject();
        json2.put("refresh_auth_data_token", refresh_auth_data_token);
        json2.put("access_token", access_token);
        JSONObject de = JingDieRenZheng.httpPosyauthdata(url3, String.valueOf(timestamp), json2);
        JSONObject authData = de.getJSONObject("data");
        String auth_data = authData.getString("auth_data");
        JSONObject extend_data = authData.getJSONObject("extend_data");
        String refresh_auth_data_tokencf = extend_data.getString("refresh_auth_data_token");

        JSONObject json= new JSONObject();
        json.put("access_token",access_token);
        json.put("gw_router_addr",gw_router_addr);
        json.put("auth_data",auth_data);//更新
        json.put("refresh_auth_data_token",refresh_auth_data_tokencf);//更新

        return json;
    }


    //实际获取接口工具类
    public static JSONObject httpPosysjhq(JSONObject data,String serverURL, String shijiancuo, JSONObject parm) throws Exception {

        String auth_data = data.getString("auth_data");
        String gw_router_addr = data.getString("gw_router_addr");
        //headers
        Map<String, String> map = new HashMap<>();
        map.put("KIS-State", "1");//请求随机数
        map.put("KIS-Timestamp", shijiancuo);//时间戳
        map.put("KIS-TraceID", "270962");//客户终端请求轨迹id
        map.put("KIS-AuthData", auth_data);//
        map.put("KIS-Ver", "1.0");//接口版本号
        map.put("X-GW-Router-Addr", gw_router_addr);//
        JSONObject jsonObject = JieKouUtil.httpDoPost(serverURL, parm, map);
        return jsonObject;
    }

}
