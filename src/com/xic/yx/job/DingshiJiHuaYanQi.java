package com.xic.yx.job;


import com.xic.util.DateUtil;
import weaver.conn.RecordSet;
import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;
import weaver.interfaces.schedule.BaseCronJob;

/**
 *营销
 * 定时获取已回款数据更新业绩目标和应收款
 */

public class DingshiJiHuaYanQi extends BaseCronJob {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void execute() {
        log.info("定时获取已回款数据");

        RecordSet rs = new RecordSet();
        RecordSet rs1 = new RecordSet();
        String sql = "select yskzjid from uf_jyqkysk";//应收款-业务员体系
        log.info(sql);
        log.info("查询进应收款-业务员体系");
        rs.executeQuery(sql);

        while (rs.next()){
            int id = rs.getInt("yskzjid");//主键
            if(id!=0) {
                String sql1 ="select yhkje from uf_ysk where id="+id;//财务应收款
                rs1.execute(sql1);
                log.info(sql1);
                // 判断ResultSet结果集是否为空
                if (rs1.next()) {
                    double yhkje = rs1.getDouble("yhkje");
                    String sqlUpdate ="update uf_jyqkysk set dcje="+yhkje+" where id="+id;
                    log.info(sqlUpdate);
                    rs1.execute(sqlUpdate);
                } else {
                    continue;
                }


            }
        }

        log.info("定时获取呆滞库存数据");

        RecordSet dzkc = new RecordSet();
        String sqldzkc = "select dzkczjid from uf_dzkcsjywy";//经营情况呆滞库存-业务员体系
        log.info(sqldzkc);
        dzkc.executeQuery(sqldzkc);
        while (dzkc.next()){
            int id = rs.getInt("dzkczjid");//主键
            if(id!=0) {
                String sql1 ="select ycksl from uf_kcsj where id="+id;//呆滞库存数据
                log.info(sql1);
                rs1.execute(sql1);
                // 判断ResultSet结果集是否为空
                if (rs1.next()) {
                    double ycksl = rs1.getDouble("ycksl");
                    String sqlUpdate ="update uf_dzkcsjywy set ycksl="+ycksl+" where id="+id;
                    log.info(sqlUpdate);
                    rs1.execute(sqlUpdate);
                } else {
                    continue;
                }
            }


        }




        log.info("获取应收款数据");

        int id = 0;
        double dcje = 0.00;
        double yqzje = 0.00;
        String jzhkrq = null;
        String bjsj = null;
        double i=0;
        String byDate=null;
        double syts=0;
        double tsjdt=0;
        double jdt=0;
        int jdtint=0;
        int tsjdtInt=0;
        int sytsInt=0;

        String sqlysk = "select id,jzhkrq,bjsj,dcje,yqzje from uf_jyqkysk";//应收款-业务员体系
        log.info(sqlysk);
        log.info("查询进应收款-业务员体系");
        rs.executeQuery(sqlysk);


        DateUtil dateUtil=new DateUtil();
        while (rs.next()) {
            id = rs.getInt("id");//主键
            dcje = rs.getDouble("dcje");//已回款金额
            yqzje = rs.getDouble("yqzje");//逾期总金额
            jzhkrq = rs.getString("jzhkrq");//截止回款日期
            bjsj = rs.getString("bjsj");//编辑时间

            if (id != 0) {
                i = dateUtil.CalculateDays(bjsj, jzhkrq);//总天数
                byDate = dateUtil.getByDate();//当前日期
                syts = dateUtil.CalculateDays(byDate, jzhkrq);//剩余天数
                tsjdt=(syts/i)*100;//天数进度条
                jdt= ((dcje/yqzje)*100);//金额进度条
                tsjdtInt=(int)tsjdt;
                jdtint=(int)jdt;
                sytsInt=(int)syts;
                String sqlUpdate = "update uf_jyqkysk set hkjd="+ jdt+",djsjdt="+tsjdtInt+",syts="+sytsInt+" where id=" + id;
                log.info(sqlUpdate);
                rs1.execute(sqlUpdate);


            }
        }
        String sqljywy = "select id,ycksl,dzkcsl,jzrq,bjsj from uf_dzkcsjywy";//应收款-业务员体系
        log.info(sqljywy);
        log.info("定时更新呆滞库存进度条");
        rs.executeQuery(sqljywy);
        while (rs.next()) {
            id = rs.getInt("id");//主键
            dcje = rs.getDouble("ycksl");//已出库数量
            yqzje = rs.getDouble("dzkcsl");//呆滞库存数量
            jzhkrq = rs.getString("jzrq");//截止日期
            bjsj = rs.getString("bjsj");//编辑时间
            if (id != 0) {
                i = dateUtil.CalculateDays(bjsj, jzhkrq);//总天数
                byDate = dateUtil.getByDate();//当前日期
                syts = dateUtil.CalculateDays(byDate, jzhkrq);//剩余天数
                tsjdt=(syts/i)*100;//天数进度条
                jdt=(dcje/yqzje)*100;//出库进度条
                tsjdtInt=(int)tsjdt;
                jdtint=(int)jdt;
                sytsInt=(int)syts;

                String sqlUpdate = "update uf_dzkcsjywy set syts="+ sytsInt+",djsjdt="+tsjdtInt+",ckjd="+jdtint+" where id=" + id;
                System.out.println(sqlUpdate);
                log.info(sqlUpdate);
                rs1.execute(sqlUpdate);

            }


        }

    }
}
