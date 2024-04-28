package com;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import com.api.xic.jd.web.RenZhengController;
import com.oneWorld.ecology.common.http.ServletRequestUtil;
import com.xic.pm.workflow.action.KeYanLiXiangAction;
import com.xic.yx.job.CrmKeHuDizhi;
import com.xic.yx.job.SalesPerformanceJob;
import com.xic.yx.job.TwoprogressbarsJob;
import com.xic.zhouhui.jianmo.MuBiaoJiFen;
import weaver.general.GCONST;
import weaver.soa.workflow.request.RequestInfo;
import weaver.soa.workflow.request.RequestService;


public class test {


    public static void main(String[] args) throws Exception {
        GCONST.setServerName("ecology");
        GCONST.setRootPath("D:/WEAVER/ecology/WEB-INF/");

        //api测试
        getLeaveDaysapi();
        //计划任务
        getLeaveDaysjhr();
        //流程
        //getLeaveDayslc();
        //建模
        //getJianMO();
        System.out.println("执行完成");
    }

    //api测试
    public static void getLeaveDaysapi() throws Exception {

        //弄一个reqeust
        ServletRequestUtil requestUtil = new ServletRequestUtil();
        //赋值
        HttpServletRequest request = requestUtil.getRequestHasUser();
        Map<String, String[]> param = request.getParameterMap();
        param.put("hash", new String[]{"#/main/portal/portal-58-31"});
//        param.put("yjkssj", new String[]{"2024-02-20"});

      /*
       beginTime: 2021-10-25 14:49
endTime: 2021-10-28 14:49
qjlx: cb51db97b6b8413aad5c2a60ac009aff
sqr: 17876

       beginTime=2021-10-25+14%3A49&endTime=2021-10-28+14%3A49&qjlx=cb51db97b6b8413aad5c2a60ac009aff&sqr=17876
      */

       // SqlByMySelf controller = new SqlByMySelf();
        //String ret = controller.sendMail(request, null);

       // System.out.println(ret);

        RenZhengController renZhengController = new RenZhengController();
        renZhengController.BaoCun(request, null);
        System.out.println( renZhengController.BaoCun(request, null));
    }
    //计划任务
    public static void getLeaveDaysjhr() {
     //   YingShouKuanYuQi fi = new YingShouKuanYuQi();
//        fi.execute();
       // DingshiJiHuaYanQi dingshiJiHuaYanQi =new DingshiJiHuaYanQi();
        //dingshiJiHuaYanQi.execute();
//        CrmKeHuDizhi crmKeHuDizhi=new CrmKeHuDizhi();
//        crmKeHuDizhi.execute();
//        SalesPerformanceJob salesPerformanceJob=new SalesPerformanceJob();
//        salesPerformanceJob.execute();
        TwoprogressbarsJob twoprogressbarsJob=new TwoprogressbarsJob();
        twoprogressbarsJob.execute();
    }
    //流程
    public static void getLeaveDayslc() {
        int requestid = 123311;

        //通过request获取RequestInfo
        RequestService requestService = new RequestService();
        RequestInfo requestInfo = requestService.getRequest(requestid);

        //调用那个action
        KeYanLiXiangAction aciton = new KeYanLiXiangAction();
        String s =aciton.execute(requestInfo);
        System.out.println(s);
    }
    //建模
    public static void getJianMO() {
        GCONST.setServerName("ecology");
        GCONST.setRootPath("D:/WEAVER/ecology/WEB-INF/");

        Map<String, Object> param = new HashMap<>();
        param.put("RequestInfo", "15");

        //调用那个action   PanYingDanAction
        //K3DRPurchaseSaveAction1
        //PanKuiDanAction
        MuBiaoJiFen aciton = new MuBiaoJiFen();
        Map<String, String> s = aciton.doModeExpand(param);
        System.out.println(s);
    }

}
