package com.api.xic.jd.util;

import com.util.JingDieRenZheng;
import net.sf.json.JSONObject;
import org.opensaml.xml.signature.J;

import java.util.concurrent.TimeUnit;

//金蝶基础数据所有接口
public class JieKouJdjcsj {

    //批量查询物料基础资料
    public static JSONObject wlf(JSONObject data, long timestamp,int i) throws Exception {
        String url = "https://api.kingdee.com/koas/APP006992/api/Material/List?";
        String access_token = data.getString("access_token");//全局都要用

        url+="access_token="+access_token;

        JSONObject json2 = new JSONObject();
        json2.put("CurrentPage", String.valueOf(i));//当前页
        json2.put("ItemsOfPage", "100");//一页大小
        JSONObject de = JingDieRenZheng.httpPosysjhq(data,url, String.valueOf(timestamp), json2);

        return de;
    }

    //批量查询库存数据
    public static JSONObject kucun(JSONObject data, long timestamp) throws Exception {
        String url = "https://api.kingdee.com/koas/APP002112/uereport/UEStockController/SearchItemInfors?";
        String access_token = data.getString("access_token");//全局都要用

        url+="access_token="+access_token;

        JSONObject json2 = new JSONObject();
        json2.put("CurrentPage", "1");//当前页
        json2.put("ItemsOfPage", "100");//一页大小
        JSONObject de = JingDieRenZheng.httpPosysjhq(data,url, String.valueOf(timestamp), json2);
        JSONObject datasj = de.getJSONObject("data");

        return datasj;
    }
}
