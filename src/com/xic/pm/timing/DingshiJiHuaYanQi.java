package com.xic.pm.timing;


import lombok.SneakyThrows;
import weaver.common.StringUtil;
import weaver.conn.RecordSet;
import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;
import weaver.interfaces.schedule.BaseCronJob;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * 定时判断预计结束时间<当前时间更改状态为延期
 */

public class DingshiJiHuaYanQi extends BaseCronJob {

    private Logger log = LoggerFactory.getLogger(getClass());

    @SneakyThrows
    @Override
    public void execute() {
        log.info("状态更新延期开始");
        //当前日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //当前时间
        Date date = new Date();
        RecordSet rs = new RecordSet();
        RecordSet rs1 = new RecordSet();
        String sql = "select yjjsrq,proCode from uf_projectinfo where finish != 100";
        log.info("查询进度不到100的项目，进行判断");
        rs.executeQuery(sql);
        while (rs.next()){
            String enddate = rs.getString("yjjsrq");//预计结束时间
            String xmbm = rs.getString("proCode");//项目编码
            if(StringUtil.isNotNull(enddate)) {
                Date jhwcsj;
                jhwcsj = sdf.parse(enddate);
                if(jhwcsj != null && jhwcsj.before(date)){//计划完成实际小于当前时间就是延期
                    String sql1 = "update uf_projectinfo set status=2,fazt=2 where proCode='"+xmbm+"'";
                    rs1.execute(sql1);
                }
            }

        }
    }
}
