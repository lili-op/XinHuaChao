package com.api.xic.jd.tool;

import com.util.JingDieRenZheng;
import net.sf.json.JSONObject;

public class InterfaceToolAPI {
    //时间戳
    public static long shijianchuo(){
        long djcData3 = System.currentTimeMillis() / 1000;//循环时间戳
        return djcData3;

    }
    //获取批量查询的总页数
    public  int Selectpages(int page, JSONObject data, String url) throws Exception {
        JSONObject json2 = new JSONObject();
        json2.put("CurrentPage", String.valueOf(page));//当前页
        json2.put("ItemsOfPage", "100");//一页大小
        long interval = 3000;
        Thread.sleep(interval);
        long shijianchuo = shijianchuo();
        JSONObject jsonObject = JingDieRenZheng.httpPosysjhq(data,url, String.valueOf(shijianchuo), json2);
        int pages=jsonObject.getJSONObject("data").getInt("TotalPages");
        return pages;
    }
    //获取批量查询数据
    public  JSONObject SelectData(int ii,JSONObject data,String url) throws Exception {
        JSONObject json2 = new JSONObject();
        json2.put("CurrentPage",ii);//当前页
        json2.put("ItemsOfPage", "100");//一页大小
        long interval = 3000;
        Thread.sleep(interval);
        long shijianchuo = shijianchuo();
        JSONObject jsonObject = JingDieRenZheng.httpPosysjhq(data,url, String.valueOf(shijianchuo), json2);
        return jsonObject;
    }


    //根据主键查询数据
    public JSONObject selectConditionId(JSONObject data,String url,int zid) throws Exception {
        JSONObject json = new JSONObject();
        json.put("ItemId",zid);//金蝶产品id
        long interval = 3000;
        Thread.sleep(interval);
        long shijianchuo = shijianchuo();
        JSONObject jsonObject = JingDieRenZheng.httpPosysjhq(data,url, String.valueOf(shijianchuo), json);
        return jsonObject;
    }

}
