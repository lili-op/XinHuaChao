package com.xic.zhouhui.timing;

import com.weaver.general.TimeUtil;
import lombok.SneakyThrows;
import weaver.conn.BatchRecordSet;
import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;
import weaver.interfaces.schedule.BaseCronJob;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 *  应收款数据插入营销周会
 */

public class YingShouKuan extends BaseCronJob {

    private Logger log = LoggerFactory.getLogger(getClass());

    @SneakyThrows
    @Override
    public void execute() {
        log.info("开始提取本周周会应收款数据");
        try {
            BatchRecordSet brs = new BatchRecordSet();
            List<List<Object>> listInsert = new ArrayList<>();//批量插入
            String formmodeid = "154";
            String nowDate = TimeUtil.getCurrentDateString();
            String nowTime = TimeUtil.getOnlyCurrentTimeString();
            String sql;
            String mzlrq = getSaturdayDate();//每周六日期

            RecordSet rs = new RecordSet();
            //查询营销周会每周六日期主键id
            sql = "select id from uf_yxzhhyzhub where mzlrq = '"+mzlrq+"'";
            rs.execute(sql);
            String mzlrqid = "";
            if(rs.next()){
                mzlrqid = rs.getString("id");
            }


            sql = "select max(id) as id from uf_yxzhhyysk";
            rs.execute(sql);
            int kaishiId = 0;
            if(rs.next()){
                kaishiId = rs.getInt("id");
            }


             sql = "select fzr,bm,khmc,yqje from uf_ysk";
            rs.execute(sql);
            while (rs.next()){
                String fzr = rs.getString("fzr");//负责人
                String bm = rs.getString("bm");//部门
                String khmc = rs.getString("khmc");//客户名称
                String yqje = rs.getString("yqje");//逾期总金额

                listInsert.add(Arrays.asList(mzlrq,mzlrqid,fzr, bm, khmc, yqje, formmodeid, 1, 0,
                        nowDate, nowTime, UUID.randomUUID().toString()));
            }

            String insertsql = "insert into uf_yxzhhyysk " +
                    "(mzlrqxs,mzlrq,fzr,gbumc,yqkh,yqje,formmodeid,modedatacreater,modedatacreatertype," +
                    "modedatacreatedate,modedatacreatetime, MODEUUID) " +
                    "VALUES " +
                    "(?,?,?,?,?,?,?,?,?,?,?,?)";
            if (listInsert.size() > 0) {
                brs.executeBatchSql(insertsql, listInsert);
            }


            //插入完成查询最大id
            sql = "select max(id) as id from uf_yxzhhyysk";
            rs.execute(sql);
            int zuihouId = 0;
            if(rs.next()){
                zuihouId = rs.getInt("id");
            }

            //刷权限
            ModeRightInfo modeRightInfo = new ModeRightInfo();
            for(int i=kaishiId;i<=zuihouId;i++){
                modeRightInfo.rebuildModeDataShareByEdit(1, Integer.parseInt(formmodeid), i);
            }

        } catch (Exception e) {
            log.info("插入周会,报错信息:", e);
        }
    }

    public static String getSaturdayDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            cal.set(Calendar.DATE,cal.get(Calendar.DATE)-1);
            return simpleDateFormat.format(cal.getTime());
        }
        cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        cal.set(Calendar.DATE,cal.get(Calendar.DATE)+5);
        return simpleDateFormat.format(cal.getTime());
    }
}
