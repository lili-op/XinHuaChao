package com.api.xic.jd.jdapi;



import com.api.xic.jd.tool.InterfaceToolAPI;
import com.api.xic.jd.tool.InvokeAPI;
import com.util.JingDieRenZheng;
import com.weaver.general.TimeUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.exolab.castor.builder.binding.Interface;
import weaver.conn.RecordSet;

import java.util.*;

//同步金蝶库存的数据
public class HttpHDInventory {

    //批量查询销售出库单
    public static int wlf(JSONObject data, long timestamp,int page) throws Exception {

        String access_token = data.getString("access_token");//全局都要用
       InvokeAPI invokeAPI=new InvokeAPI();
        InterfaceToolAPI interfaceTool=new InterfaceToolAPI();
        RecordSet rs = new RecordSet();
        String url= invokeAPI.Sxscks("Sjskcs")+access_token;
        int pages = interfaceTool.Selectpages(page,data,url);

        //泛微建模标准字段
        String formmodeid = "238";//模块id
        String nowDate = TimeUtil.getCurrentDateString();
        String nowTime = TimeUtil.getOnlyCurrentTimeString();

        outer:for(int i=0;i<=pages;i++){//根据总页数循环
            int ii=i+1;
            String url2= invokeAPI.Sxscks("Sjskcs")+access_token;
           JSONObject da2= interfaceTool.SelectData(ii,data,url2);
           JSONArray jsonObj = da2.getJSONObject("data").getJSONArray("List");
            // 计数
            Iterator<Object> it = jsonObj.iterator();
            int count=0;
                while(it.hasNext()){//根据循环的当前页去循环里面的页数
                    UUID uuid = UUID.randomUUID();//主键
                    count++;
                    if(count<100){
                        JSONObject jsonObjec=(JSONObject) it.next();
                        int FMaterialID = jsonObjec.getInt("FMaterialID");//物料id
                        int FBUQty = jsonObjec.getInt("FBUQty");//基本单位数量
                        //根据产品id查询产品名称
                        long djcData5 = System.currentTimeMillis() / 1000;//循环时间戳

                        String url3= invokeAPI.Sxscks("Swlxx")+access_token;
                        JSONObject jsonObject = interfaceTool.selectConditionId(data, url3, FMaterialID);

                        String wldm=jsonObject.getJSONObject("data").getString("FNumber");//物料代码
                        String xhmc=jsonObject.getJSONObject("data").getString("FName");//型号名称
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
        return pages;
    }
}
