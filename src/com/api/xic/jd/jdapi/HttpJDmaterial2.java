package com.api.xic.jd.jdapi;

import com.api.xic.jd.tool.InterfaceToolAPI;
import com.api.xic.jd.tool.InvokeAPI;
import com.weaver.general.TimeUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import weaver.conn.RecordSet;

import java.util.Iterator;
import java.util.UUID;

//同步更新金蝶物料的数据
public class HttpJDmaterial2 {

    //同步金蝶物料的数据
    public static int wlf(JSONObject data, long timestamp, int page) throws Exception {

        String access_token = data.getString("access_token");//全局都要用
        InvokeAPI invokeAPI=new InvokeAPI();
        InterfaceToolAPI interfaceTool=new InterfaceToolAPI();
        RecordSet rs = new RecordSet();
        String url= invokeAPI.Sxscks("Swlxxs")+access_token;
        int pages = interfaceTool.Selectpages(page,data,url);

        //泛微建模标准字段
        String formmodeid = "54";//模块id
        String nowDate = TimeUtil.getCurrentDateString();
        String nowTime = TimeUtil.getOnlyCurrentTimeString();

        outer:for(int i=0;i<=pages;i++){//根据总页数循环
            int ii=i+1;
            String url2= invokeAPI.Sxscks("Swlxxs")+access_token;
            JSONObject da2= interfaceTool.SelectData(ii,data,url2);
            JSONArray jsonArray = da2.getJSONObject("data").getJSONArray("List");   // 计数
            Iterator<Object> it = jsonArray.iterator();
            int size = jsonArray.size();
            int fItemID =jsonArray.getJSONObject(1).getInt("FItemID");

            // 计数
            int count=0;
            while(it.hasNext()){//根据循环的当前页去循环里面的页数
                UUID uuid = UUID.randomUUID();//主键
                count++;
                if(count<100){
                    JSONObject jsonObjec=(JSONObject) it.next();
                    int FItemID = jsonObjec.getInt("FItemID");//物料id
                   // String FNumber = data1.getString("FNumber");//物料代码
                    //根据产品id查询产品名称
                        //根据物料代码去查询oa的物料数据

                        String sqlry= "select wldm,cpxh from uf_xhcjbxx where wldm is null";
                        rs.execute(sqlry);
                        String cpxh = null;
                        if(rs.next()) {
                            cpxh=rs.getString("cpxh");//物料代码
                            String url3= invokeAPI.Sxscks("Swlxx")+access_token;
                            JSONObject jsonObject = interfaceTool.selectConditionId(data, url3, FItemID);
                            String fName = jsonObject.getString("FName");//产品型号名称
                            String FNumber = jsonObject.getString("FNumber");//产品型号名称
                            if(cpxh.equals(fName)){//相等修改采购价
                                String sqlupdate="update uf_xhcjbxx set wldm="+fName+"where cpxh="+FNumber;
                                rs.execute(sqlupdate);

                            }else if(!cpxh.equals(fName)) {
                                continue ;
                            }
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
