package com.api.xic.jd.web;



import com.api.xic.jd.jdapi.*;

import com.util.JingDieRenZheng;
import com.xic.pm.workflow.bean.CommonResult;
import net.sf.json.JSONObject;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author tangaofeng
 * @Description 2.金蝶认证(获取到token)code
 * @since 2022-9-16
 */
@Path("/xhc/pm/lc")
public class RenZhengController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    //时间戳
    public static long shijianchuo(){
        long djcData3 = System.currentTimeMillis() / 1000;//循环时间戳
        return djcData3;

    }

    @POST
    @Path("/BaoCun")
    @Produces({MediaType.APPLICATION_JSON})
    public CommonResult BaoCun(@Context HttpServletRequest request, @Context HttpServletResponse response) throws Exception {

       // long timestamp = System.currentTimeMillis() / 1000;

        JSONObject json= new JSONObject();
        json.put("code","1714280835707b5b8dffff22e437efed");
        JSONObject coke = JingDieRenZheng.getCoke(json);
//        String startDate=request.getParameter("time1");
//        String endDate=request.getParameter("time2");
//        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate startDate2 = LocalDate.parse(startDate, df);
//        LocalDate endDate2 = LocalDate.parse(endDate, df);
//        LocalDate startDate2=null;
//        LocalDate endDate2=null;

        String access_token = coke.getString("access_token");//全局都要用
        String session_id = coke.getString("session_id");


        //单个账套
        String stringArray = "UE162828052021072818041314R";
        long timestamp =shijianchuo();
        JSONObject data = JingDieRenZheng.gettoken(stringArray,session_id,access_token);//获取到token和sessiontoken和auth_data
        int wlf = HttpJDmaterial.wlf(data, timestamp, 1);
        //调用批量查询物料的接口
        System.out.println("物料"+wlf);

//        //多个账套
//        String[] stringArray = {"UE162828052021072818041314R", "UE162828052021072709481828R", "UE162828052021111014415144"};
//        int[] ztid = {0,1,2};
//        for (int i = 0; i < stringArray.length; i++) {
//            long timestamp =shijianchuo();
//            JSONObject data = JingDieRenZheng.gettoken(stringArray[i],session_id,access_token);//获取到token和sessiontoken和auth_data
//            System.out.println("數量"+i);
//            JSONObject jsonObject5= HttpBatchQuery2.wlf(data,timestamp,1,ztid[i]);//调用批查询销售出库的接口
//            System.out.println("销售出库"+jsonObject5);
//        }
        //JSONObject data = JingDieRenZheng.gettoken(json,timestamp,acctnumber);//获取到token和sessiontoken和auth_data

//        JSONObject jsonObject = JieKouJdjcsj.wlf(data,timestamp,1);//调用批量查询物料接口
//        System.out.println("数量1"+jsonObject);
//
//        JSONObject data2 = JingDieRenZheng.gengxauthdata(data,timestamp);
//        JSONObject jsonObject2 = JieKouJdjcsj.wlf(data2,timestamp,2);//调用批量查询物料接口
//        System.out.println("数量2"+jsonObject2);
//
////        JSONObject data3 = JingDieRenZheng.gengxauthdata(data,timestamp);
////        JSONObject jsonObject3 = JieKouJdjcsj.wlf(data3,timestamp,3);//调用批量查询物料接口
////        System.out.println("数量3"+jsonObject3);
////
//        JSONObject data4 = JingDieRenZheng.gengxauthdata(data,timestamp);
//        JSONObject jsonObject4 = JieKouJdjcsj.wlf(data4,timestamp,4);//调用批量查询物料接口
//        System.out.println("数量4"+jsonObject4);
        //做插入台账或者拼接

        //JSONObject jsonObject = JieKouJdjcsj.kucun(data,timestamp);//调用批量查询库存接口


//        int jsonObject5= HttpHDInventory.wlf(data,timestamp,1);//调用批量查询库存的接口
//        System.out.println("库存"+jsonObject5);



        return new CommonResult();
    }

}
