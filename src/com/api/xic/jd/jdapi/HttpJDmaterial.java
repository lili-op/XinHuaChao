package com.api.xic.jd.jdapi;

import com.api.xic.jd.tool.InterfaceToolAPI;
import com.api.xic.jd.tool.InvokeAPI;
import com.util.JingDieRenZheng;
import com.weaver.general.TimeUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import weaver.conn.RecordSet;

import java.util.*;

//同步更新金蝶物料的数据
public class HttpJDmaterial {

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
        String str=null;
        outer:for(int i=0;i<=pages;i++){//根据总页数循环
            int ii=i+1;
            String url2= invokeAPI.Sxscks("Swlxxs")+access_token;
            JSONObject da2= interfaceTool.SelectData(ii,data,url2);
            JSONArray data1 = da2.getJSONObject("data").getJSONArray("List");

            // 计数

            int count=0;
         for(int j=0;i<=data1.size();j++){//根据循环的当前页去循环里面的页数
                UUID uuid = UUID.randomUUID();//主键
                count++;
                if(count<100){

                    int FItemID = data1.getJSONObject(i).getInt("FItemID");//物料id
                    String FNumber =data1.getJSONObject(i).getString("FNumber");//物料id
                    //根据产品id查询产品名称

                    if(!FNumber.equals("")||FNumber!=""){
                        //根据物料代码去查询oa的物料数据
                        RecordSet res = new RecordSet();
                        String sqlry= "select wldm from uf_xhcjbxx where wldm='"+FNumber+"'";
                        res.execute(sqlry);
                        String wudm = null;
                        if(res.next()) {
                            wudm=res.getString("wldm");//物料代码
                        }

                        String url3= invokeAPI.Sxscks("Swlxx")+access_token;
                        JSONObject jsonObject = interfaceTool.selectConditionId(data, url3, FItemID);
                        String fName = jsonObject.getJSONObject("data").getString("FName");//产品型号名称
                        String FFullName = jsonObject.getJSONObject("data").getString("FFullName");//物料全名

                        String FOrderPrice = jsonObject.getJSONObject("data").getString("FOrderPrice");//采购单价

                        boolean contains=FFullName.contains("_");
                        if(contains){
                            str = FFullName.substring(0, FFullName.lastIndexOf("_"));
                        }else if(contains==false){
                            str=FFullName;
                        }
                        double FOrderPrice2=0.00;
                        double xjj=0.00;
                        double yj30=0.00;
                        double yj60=0.00;
                        double yj90=0.00;
                        if(FOrderPrice==null){
                            FOrderPrice2=Double.valueOf(FOrderPrice);
                             xjj = FOrderPrice2/0.95;//销售单价
                             yj30=FOrderPrice2/0.92;
                             yj60=FOrderPrice2/0.90;
                             yj90=FOrderPrice2/0.85;


                            if(FNumber.equals(wudm)){//相等修改采购价
                                String sqlupdate="update uf_xhcjbxx set cgj="+FOrderPrice2+",xjj="+xjj+",yj30t="+yj30+",yj60t="+yj60+",yj90t="+yj90+",ztmc=0,where wldm="+wudm;
                                rs.execute(sqlupdate);

                            }else if(!FNumber.equals(wudm)){
                                String  mxbsql= String.format("insert into uf_xhcjbxx"+
                                                "(sccj,ppmc,cpxh,cgj,wldm,xjj,yj30t,yj60t,yj90t,ztmc,formmodeid,modedatacreater,modedatacreatertype," +
                                                "modedatacreatedate,modedatacreatetime)" +
                                                "VALUES" +
                                                "('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')",
                                        str,str,fName,FOrderPrice,xjj,yj30,yj60,yj90,0,formmodeid, 1, 0, nowDate, nowTime,uuid);
                                rs.execute(mxbsql);
                                System.out.println(mxbsql);
                            }
                        }else if(FOrderPrice!=null){

                            if(FNumber.equals(wudm)){//相等修改采购价
                                String sqlupdate="update uf_xhcjbxx set cgj="+FOrderPrice+",xjj="+xjj+",yj30t="+yj30+",yj60t="+yj60+",yj90t="+yj90+",ztmc=0,where wldm="+wudm;
                                rs.execute(sqlupdate);

                            }else if(!FNumber.equals(wudm)){
                                String  mxbsql= String.format("insert into uf_xhcjbxx"+
                                                "(sccj,ppmc,cpxh,cgj,wldm,xjj,yj30t,yj60t,yj90t,ztmc,formmodeid,modedatacreater,modedatacreatertype," +
                                                "modedatacreatedate,modedatacreatetime)" +
                                                "VALUES" +
                                                "('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')",
                                        str,str,fName,FOrderPrice,xjj,yj30,yj60,yj90,0,formmodeid, 1, 0, nowDate, nowTime,uuid);
                                rs.execute(mxbsql);
                                System.out.println(mxbsql);
                            }
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
