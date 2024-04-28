package com.api.xic.jd.jdapi;


import com.util.JingDieRenZheng;
import com.weaver.general.TimeUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import weaver.conn.RecordSet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

//同步金蝶库存的数据
public class HttpHDInventory2 {

    //批量查询销售出库单
    public static JSONObject wlf(JSONObject data, long timestamp,int page) throws Exception {

        String url = "https://api.kingdee.com/koas/APP007104/api/ICInventory/List?";
        String access_token = data.getString("access_token");//全局都要用
       // InvokeAPI
        RecordSet rs = new RecordSet();

        url+="access_token="+access_token;

        JSONObject json2 = new JSONObject();
        json2.put("CurrentPage", String.valueOf(page));//当前页
        json2.put("ItemsOfPage", "100");//一页大小
        JSONObject de = JingDieRenZheng.httpPosysjhq(data,url, String.valueOf(timestamp), json2);
        //int pages = InterfaceTool.Selectpages(page,data,url);
        int pages=de.getJSONObject("data").getInt("TotalPages");
        //泛微建模标准字段
        String formmodeid = "238";//模块id
        String nowDate = TimeUtil.getCurrentDateString();
        String nowTime = TimeUtil.getOnlyCurrentTimeString();

        outer:for(int i=0;i<=pages;i++){//根据总页数循环
            long shijianchuo = HttpBatchQuery2.shijianchuo();

            String url2 = "https://api.kingdee.com/koas/APP007104/api/ICInventory/List?";
            String access_token2 = data.getString("access_token");//全局都要用

            url2+="access_token="+access_token2;
            int ii=i+1;
            JSONObject json3 = new JSONObject();
            json3.put("CurrentPage", ii);//当前页
            json3.put("ItemsOfPage", "100");//一页大小
            JSONObject da2 = JingDieRenZheng.httpPosysjhq(data,url2, String.valueOf(shijianchuo), json3);
           // JSONObject da2=InterfaceTool.SelectData(ii,data,url);
            //int pages=Integer.parseInt(de.getString("TotalPages"));
            JSONArray jsonObj = da2.getJSONObject("data").getJSONArray("List");
            ArrayList list=new ArrayList();
            // 计数
            Iterator<Object> it = jsonObj.iterator();
            int count=0;

                while(it.hasNext()){//根据循环的当前页去循环里面的页数
                    UUID uuid = UUID.randomUUID();//主键
                    count++;
                    if(count<100){
                        JSONObject jsonObjec=(JSONObject) it.next();
                        String FMaterialID = jsonObjec.getString("FMaterialID");//物料id
                        int FBUQty = jsonObjec.getInt("FBUQty");//基本单位数量

                        //根据产品id查询产品名称
                        long shijianchuo1 = HttpBatchQuery2.shijianchuo();

                        String url5 = "https://api.kingdee.com/koas/APP006992/api/Material/GetDetail?";
                        String access_token5 = data.getString("access_token");//全局都要用
                        url5+="access_token="+access_token5;
                        JSONObject json5 = new JSONObject();
                        json5.put("ItemId",FMaterialID);//金蝶产品id
                        JSONObject de5 = JingDieRenZheng.httpPosysjhq(data,url5, String.valueOf(shijianchuo1), json5);
                        String wldm=de5.getJSONObject("data").getString("FNumber");//物料代码
                        String xhmc=de5.getJSONObject("data").getString("FName");//型号名称

                        //插入明细表
                        try{

                            String  mxbsql= String.format("insert into uf_wlkcsj"+
                                    "(ztmc,wldm,kcsl,cpxh,formmodeid,modedatacreater,modedatacreatertype," +
                                    "modedatacreatedate,modedatacreatetime)" +
                                    "VALUES" +
                                    "('%s','%s','%s','%s','%s','%s','%s','%s','%s')",0,wldm,FBUQty,xhmc,formmodeid, 1, 0, nowDate, nowTime,uuid);
                            rs.execute(mxbsql);
                            System.out.println(mxbsql);

                        }catch(Exception e) {
                            System.out.println("插入明细表出错");
                        }

                    }else{
                        continue outer;
                    }

                }

            continue;
        }
        //System.out.println(de);
        return de;
    }
}
