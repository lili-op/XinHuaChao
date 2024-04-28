//package com.api.xic.jd.jdapi;
//
//import com.api.xic.jd.tool.DataTool;
//import com.util.JingDieRenZheng;
//import com.weaver.general.TimeUtil;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//import weaver.conn.BatchRecordSet;
//import weaver.conn.RecordSet;
//import weaver.formmode.setup.ModeRightInfo;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//
////同步金蝶销售出库的数据
//public class HttpBatchQuery2 {
//    public static long shijianchuo() throws InterruptedException {
//        long interval = 3000;
//        Thread.sleep(interval);
//        long djcData3 = System.currentTimeMillis() / 1000;//循环时间戳
//        return djcData3;
//
//    }
//
//    //批量查询销售出库
//    // 单
//    public static JSONObject wlf(JSONObject data, long timestamp, int page, int ztid) throws Exception {
//
//        DataTool tool = new DataTool();
//        String nowTime1 = tool.getNowTime("yyyy-MM-dd");//當前日期
//        List<LocalDate> datesBetween = tool.getDatesBetween(startDate, endDate);
//
//        int FItemID = 0;
//        String FMapName;
//        double FConsignPrice = 0.00;
//        double FConsignAmount = 0.00;
//        int Fauxqty;
//        Map map2 = new HashMap<>();
//        int FTaxRate = 0;
//        double xsjefds=0.00;
////修改权限
//        ModeRightInfo modeRightInfo = new ModeRightInfo();
//        String url = "https://api.kingdee.com/koas/app007104/api/salesdelivery/list?";
//        String access_token = data.getString("access_token");//全局都要用
//        String insertsql = null;
//        String mxbsql = null;
//        url += "access_token=" + access_token;
//        List<List<Object>> lists = new ArrayList<>();//批量插入
//        List<List<Object>> listInsert = new ArrayList<>();//批量插入
//        JSONObject json2 = new JSONObject();
//        json2.put("CurrentPage", String.valueOf(page));//当前页
//        json2.put("ItemsOfPage", "100");//一页大小
//        JSONObject de = JingDieRenZheng.httpPosysjhq(data, url, String.valueOf(timestamp), json2);
//        int pages = de.getJSONObject("data").getInt("TotalPages");
//        //泛微建模标准字段
//        String formmodeid = "241";//模块id
//        String nowDate = TimeUtil.getCurrentDateString();
//        String nowTime = TimeUtil.getOnlyCurrentTimeString();
//        BatchRecordSet batchRecordSet = new BatchRecordSet();
//        String sql;
//        RecordSet rs = new RecordSet();
//
//        sql = "select max(id) as id from uf_yxzhhyysk";
//        rs.execute(sql);
//        int kaishiId = 0;
//        if (rs.next()) {
//            kaishiId = rs.getInt("id");
//        }
//        int jdid = 0;//金蝶出庫id
//        RecordSet res = new RecordSet();
//        outer:
//        for (int i = 0; i <= pages; i++) {//根据总页数循环
//            //long djcData = System.currentTimeMillis() / 1000;//循环时间戳
//            long djcData = shijianchuo();
//
//            String url2 = "https://api.kingdee.com/koas/app007104/api/salesdelivery/list?";
//            String access_token2 = data.getString("access_token");//全局都要用
//
//            url2 += "access_token=" + access_token2;
//
//            JSONObject json3 = new JSONObject();
//            int ii = i + 1;
//            json3.put("CurrentPage", ii);//当前页
//            json3.put("ItemsOfPage", "100");//一页大小
//            JSONObject da2 = JingDieRenZheng.httpPosysjhq(data, url2, String.valueOf(djcData), json3);
//            //int pages=Integer.parseInt(de.getString("TotalPages"));
//            JSONArray jsonObj = da2.getJSONObject("data").getJSONArray("List");
//            ArrayList list = new ArrayList();
//            // 计数
//            Iterator<Object> it = jsonObj.iterator();
//            int count = 0;
//
//
//            while (it.hasNext()) {//根据循环的当前页去循环里面的页数
//                UUID uuid = UUID.randomUUID();//主键
//                count++;
//                if (count < 100) {
//                    JSONObject jsonObjec = (JSONObject) it.next();
//                    Map map = new HashMap<>();
//                    String fSettleDate = jsonObjec.getJSONObject("Head").getString("Fdate");
//                    System.out.println("fSettleDate：" + fSettleDate);
//                    if (fSettleDate != null) {
//                        String sub2 = fSettleDate.substring(0, 4); // 截取
//                        String rq = fSettleDate.substring(0, 10); // 截取
//                        //int n= Integer.parseInt(sub2);
//                        //if(rq.equals(nowTime1)){
//                        //if(sub2.equals("2024")){
//                        for (LocalDate localDate : datesBetween) {
//                            if (rq.equals(localDate)) {
//                                int anInt = jsonObjec.getJSONObject("Head").getInt("FEmpID");//业务员
//                                int FSupplyID = jsonObjec.getJSONObject("Head").getInt("FSupplyID");//购货单位
//                                int FInterID = jsonObjec.getJSONObject("Head").getInt("FInterID");//出庫ID
//                                //銷售出庫的主id
//                                String sqlxsck = "select jdid from uf_xsckbdsj  where jdid=" + FInterID;
//                                res.execute(sqlxsck);
//                                while (res.next()) {
//                                    jdid = res.getInt("jdid");//出庫id
//                                    if (jdid == 0) {
//                                        //根据人员编号查询金蝶人员
//                                        String url3 = "https://api.kingdee.com/koas/APP006992/api/Employee/GetDetail?";
//                                        String access_token3 = data.getString("access_token");//全局都要用
//
//                                        url3 += "access_token=" + access_token3;
//
//                                        JSONObject json4 = new JSONObject();
//                                        json4.put("ItemId", anInt);//金蝶人员id
//                                        //  long djcData3 = System.currentTimeMillis() / 1000;//循环时间戳
//
//
//                                        long shijianchuo = shijianchuo();
//                                        JSONObject de3 = JingDieRenZheng.httpPosysjhq(data, url3, String.valueOf(shijianchuo), json4);
//                                        String rydm = de3.getJSONObject("data").getString("FNumber");
//                                        System.out.println(rydm);
//                                        int ryid = 0;
//                                        int departmentid = 0;
//                                        int bmid = 0;
//                                        if (rydm != null) {
//                                            //根据人员编号查询在oa查询人员id
//
//                                            String sqlry = "select id,departmentid from HrmResource where workcode='" + rydm + "'";
//                                            res.execute(sqlry);
//
//                                            if (res.next()) {
//                                                ryid = res.getInt("id");//人员id
//                                                departmentid = res.getInt("departmentid");//部门id
//                                                if (departmentid == 225 || departmentid == 254 || departmentid == 224 || departmentid == 222 || departmentid == 242 || departmentid == 230 || departmentid == 235 || departmentid == 223) {
//                                                    bmid = departmentid;
//                                                } else if (departmentid != 225 || departmentid != 254 || departmentid != 224 || departmentid != 222 || departmentid != 242 || departmentid != 230 || departmentid != 235 || departmentid != 223) {
//                                                    //查询部门上级
//                                                    String bmsql = "select supdepid from hrmdepartment where id=" + departmentid;
//                                                    res.execute(bmsql);
//                                                    if (res.next()) {
//                                                        bmid = Integer.parseInt(res.getString("supdepid"));//部门id
//                                                    }
//                                                }
//
//
//                                            }
//                                        }
//                                        //根据客户id查询金蝶客户名称
//                                        String url4 = "https://api.kingdee.com/koas/APP006992/api/Customer/GetDetail?";
//                                        String access_token4 = data.getString("access_token");//全局都要用
//
//                                        url4 += "access_token=" + access_token4;
//
//                                        JSONObject json44 = new JSONObject();
//                                        json44.put("ItemId", FSupplyID);//金蝶客户id
//                                        //long djcData66 = System.currentTimeMillis() / 1000;//循环时间戳
//                                        long interval2d = 3000;
//                                        Thread.sleep(interval2d);
//
//                                        long shijianchuo2 = shijianchuo();
//                                        JSONObject de4 = JingDieRenZheng.httpPosysjhq(data, url4, String.valueOf(shijianchuo2), json44);
//                                        String rydm4 = de4.getJSONObject("data").getString("FName");//客户名称
//                                        String FNumber = de4.getJSONObject("data").getString("FNumber");//客户代码
//                                        if (FNumber.equals("XIC0001") || FNumber.equals("XIC0002") || FNumber.equals("XIC0003")) {
//                                            continue;
//                                        } else if (!FNumber.equals("XIC0001") || !FNumber.equals("XIC0002") || !FNumber.equals("XIC0003")) {    //客户不等于芯车和新富强
//                                            //插入主表
//                                            try {
//
////
////                                        listInsert.add(Arrays.asList(ryid,departmentid,rydm4,rq,formmodeid, 1, 0,
////                                                nowDate, nowTime));
////                                     insertsql="insert into uf_xsckbdsj" +
////                                            "(xsry,xsbm,khmc,xsckrq,formmodeid,modedatacreater,modedatacreatertype," +
////                                                             "modedatacreatedate,modedatacreatetime)" +
////                                            "VALUES"+
////                                            "(?,?,?,?,?,?,?,?,?)";
//                                                insertsql = String.format("insert into uf_xsckbdsj" +
//                                                        "(ztmc,xsry,xsbm,khmc,khdm,xsckrq,jdid,formmodeid,modedatacreater,modedatacreatertype," +
//                                                        "modedatacreatedate,modedatacreatetime,MODEUUID)" +
//                                                        "VALUES" +
//                                                        "('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')", ztid, ryid, bmid, rydm4, FNumber, rq, FInterID, formmodeid, 1, 0, nowDate, nowTime, uuid);
//                                                rs.execute(insertsql);
//                                                System.out.println(insertsql);
//                                                //刷权限
//                                                String sql1 = "select id from uf_xsckbdsj where modeuuid = ?";
//                                                rs.executeQuery(sql1, uuid);
//                                                while (rs.next()) {
//                                                    modeRightInfo.rebuildModeDataShareByEdit(1, Integer.parseInt(formmodeid), Integer.parseInt(rs.getString("id")));
//                                                }
//
//                                            } catch (Exception e) {
//                                                System.out.println("主表插入失败");
//                                            }
//                                            //产品循环
//                                            JSONArray entryjson = jsonObjec.getJSONArray("Entry");
//                                            Iterator<Object> entrys = entryjson.iterator();
//
//
//                                            while (entrys.hasNext()) {
//                                                JSONObject entryJson = (JSONObject) entrys.next();
//                                                FItemID = entryJson.getInt("FItemID");//产品代码
//
//                                                long intervals = 3000;
//                                                Thread.sleep(intervals);
//
//                                                //根据产品id查询产品名称
//                                                // Long djcData5 = System.currentTimeMillis() / 1000;//循环时间戳
//
//
//                                                String url5 = "https://api.kingdee.com/koas/APP006992/api/Material/GetDetail?";
//                                                String access_token5 = data.getString("access_token");//全局都要用
//
//                                                url5 += "access_token=" + access_token5;
//
//                                                JSONObject json5 = new JSONObject();
//                                                json5.put("ItemId", FItemID);//金蝶产品id
//                                                long djcData5 = shijianchuo();
//                                                JSONObject de5 = JingDieRenZheng.httpPosysjhq(data, url5, String.valueOf(djcData5), json5);
//                                                String wldm = de5.getJSONObject("data").getString("FNumber");//物料代码
//                                                String xhmc = de5.getJSONObject("data").getString("FName");//型号名称
//
//                                                FConsignPrice = entryJson.getDouble("FConsignPrice");//销售单价
//                                                FConsignAmount = entryJson.getDouble("FConsignAmount");//销售金额
//                                                xsjefds = entryJson.getDouble("FConsignAmount");//销售金额(浮点数)
//                                                Fauxqty = entryJson.getInt("Fauxqty");//实发数量
//                                                //FTaxRate=entryJson.getInt("FTaxRate");//稅率
//                                                //插入明细表
//                                                try {
//                                                    //插入完成查询最大id
//                                                    sql = "select max(id) as id from uf_xsckbdsj";
//                                                    rs.execute(sql);
//                                                    int zuihouId = 0;
//                                                    if (rs.next()) {
//                                                        zuihouId = rs.getInt("id");
//                                                    }
//
//                                                    //lists.add(Arrays.asList(xhmc,wldm,Fauxqty,FConsignPrice,FConsignAmount,zuihouId));
////                                         mxbsql="insert into uf_xsckbdsj_dt1"+
////                                                "(xhmc,wldm,xssl,xsdj,xsje,mainid)" +
////                                                "VALUES" +
////                                                "(?,?,?,?,?,?)";
//                                                    mxbsql = String.format("insert into uf_xsckbdsj_dt1" +
//                                                            "(xhmc,wldm,xssl,xsdj,xsje,mainid,xsjefds)" +
//                                                            "VALUES" +
//                                                            "('%s','%s','%s','%s','%s','%s')", xhmc, wldm, Fauxqty, FConsignPrice, FConsignAmount, zuihouId,xsjefds);
//                                                    rs.execute(mxbsql);
//                                                    System.out.println(mxbsql);
//
//                                                } catch (Exception e) {
//                                                    System.out.println("插入明细表出错");
//                                                }
//                                            }
//                                        }
//                                    } else {
//                                        continue;
//                                    }
//                                }
//
//
//                            }
//                        }
//
//
//                    }
//                } else {
//                    continue outer;
//                }
//
//            }
//
//            continue;
//        }
//
//
////        //插入完成查询最大id
////        sql = "select max(id) as id from uf_yxzhhyysk";
////        rs.execute(sql);
////        int zuihouId = 0;
////        if(rs.next()){
////            zuihouId = rs.getInt("id");
////        }
//
//        return de;
//    }
//
//}
