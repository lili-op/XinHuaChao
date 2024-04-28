package com.xic.pm.workflow.action;

import com.oneWorld.ecology.mode.utils.FormmodeUtil;
import com.oneWorld.ecology.workflow.utils.ActionUtils;
import com.weaver.general.TimeUtil;
import weaver.conn.BatchRecordSet;
import weaver.conn.RecordSet;
import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.util.*;

/**
 * @author gaofeng
 * @description
 * @since 2022/09/13 到达制样节点说明需要制样自动吧任务插入到台账
 */
public class KeYanLiXiangAction implements Action {

    //集成日志
    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public String execute(RequestInfo requestInfo) {
        //主表字段
        ActionUtils actionUtils = new ActionUtils();
        Map<String, String> mapMain = actionUtils.getMainData(requestInfo);
        String lcbh = actionUtils.getMapValue(mapMain, "lcbh");//流程编号-招聘需求编号

        String retVal = SUCCESS;//返回流程打印
        try {
            BatchRecordSet brs = new BatchRecordSet();
            RecordSet res = new RecordSet();
            List<List<Object>> listInsert = new ArrayList<>();//批量插入
            FormmodeUtil formmodeUtil = new FormmodeUtil();
            String formmodeid = formmodeUtil.getModeidName4Table("uf_taskinfo");
            String nowDate = TimeUtil.getCurrentDateString();
            String nowTime = TimeUtil.getOnlyCurrentTimeString();

            String rwfzr = "223";//任务负责人
            String rwmc = "";//任务名称
            String xmname = "";//项目名称(通过项目编码查询台账对应的数据id)

            String sql = "select id from uf_projectinfo where proCode='" + lcbh + "'";
            res.executeQuery(sql);
            if (res.next()) {
                xmname = res.getString("id");
            }

            rwmc = "测试";//任务名称
            listInsert.add(Arrays.asList(xmname, rwfzr, rwmc, formmodeid, 1, 0,
                    nowDate, nowTime, UUID.randomUUID().toString()));

            rwmc = "调试";//任务名称
            listInsert.add(Arrays.asList(xmname, rwfzr, rwmc, formmodeid, 1, 0,
                    nowDate, nowTime, UUID.randomUUID().toString()));

            rwmc = "制样";//任务名称
            listInsert.add(Arrays.asList(xmname, rwfzr, rwmc, formmodeid, 1, 0,
                    nowDate, nowTime, UUID.randomUUID().toString()));

            rwmc = "打样";//任务名称
            listInsert.add(Arrays.asList(xmname, rwfzr, rwmc, formmodeid, 1, 0,
                    nowDate, nowTime, UUID.randomUUID().toString()));


            String insertsql = "insert into uf_taskinfo " +
                    "(prjid,hrmid,subject,formmodeid,modedatacreater,modedatacreatertype," +
                    "modedatacreatedate,modedatacreatetime, MODEUUID) " +
                    "VALUES " +
                    "(?,?,?,?,?,?,?,?,?)";
            if (listInsert.size() > 0) {
                brs.executeBatchSql(insertsql, listInsert);
            }

        } catch (Exception e) {
            requestInfo.getRequestManager().setMessagecontent("流程错误，请联系系统管理员");
            log.info("插入任务列表,报错信息:", e);
            retVal = FAILURE_AND_CONTINUE;
        }
        return retVal;
    }
}
