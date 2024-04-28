package com.xic.yx.job;

import com.xic.util.DateUtil;
import weaver.conn.RecordSet;
import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;
import weaver.interfaces.schedule.BaseCronJob;

//经营情况Sales业绩-业务员体系
public class SalesPerformanceJob extends BaseCronJob {
    private Logger log = LoggerFactory.getLogger(getClass());
    @Override
    public void execute() {
        log.info("定时获取Sales业绩");

        RecordSet rs = new RecordSet();
//        String sql = "select id,xsje from uf_xsckbdsj_dt1";
//        rs.executeQuery(sql);
//        while (rs.next()){
//            int id=rs.getInt("id");
//            String xsje = rs.getString("xsje");
//            String updates="update uf_xsckbdsj_dt1 set xsjefds="+xsje+" where id="+id;
//            rs.execute(updates);
//
//        }


        RecordSet rs1 = new RecordSet();
        String sql = "select id,zrr,yjdc,yjyf,jsrq from uf_salesyjywy";//应收款-业务员体系
        log.info(sql);
        log.info("查询Sales业绩--业务员体系");
        rs.executeQuery(sql);
        int zrr=0;//责任人
        int yjdc=0;//业绩达成
        int yjyfid=0;//业绩月份表id
        String yjyf=null;//业绩月份
        double zje=0.00;//总金额
        int jdt=0;//进度条（整数）
        int id=0;//主键
        String jsrq=null;//结束日期
        String ksrq=null;//开始日期
        int i=0;
        int syts=0;
        int tsjdt=0;
        DateUtil dateUtil=new DateUtil();
        while (rs.next()){
            id = rs.getInt("id");
            zrr = rs.getInt("zrr");//人员id
            yjdc=rs.getInt("yjdc");//业绩达成
            yjyfid=rs.getInt("yjyf");//业绩月份
            jsrq=rs.getString("jsrq");//结束日期
            String sql3 = "select yf from  uf_yf where id="+yjyfid;//月份表
            rs1.execute(sql3);
            while (rs1.next()){
                yjyf=rs1.getString("yf");
            }
            ksrq = yjyf + "-1";
            if(zrr!=0) {
                i = dateUtil.CalculateDays(ksrq, jsrq);//总天数
                String byDate = dateUtil.getByDate();
                syts = dateUtil.CalculateDays(byDate, jsrq);//剩余天数
                tsjdt=(syts/i)*100;//天数进度条

                //计算这个人这个月的总金额
                String sql1 ="select SUM(xsjefds)as zje from uf_xsckbdsj j left join uf_xsckbdsj_dt1 dt on (j.id=dt.mainid) where xsry="+zrr +"and xsckrq like '"+yjyf+"%'";
                rs1.execute(sql1);
                log.info(sql1);
                // 判断ResultSet结果集是否为空
                if (rs1.next()) {
                    zje = rs1.getDouble("zje");
                    jdt= (int) (zje/yjdc)*100;
                    //更新数据
                    String sqlUpdate ="update uf_salesyjywy set dcje="+zje+",yjdcjd="+jdt+",djsjdt="+tsjdt+",syts="+syts+" where id="+id;
                    log.info(sqlUpdate);
                    rs1.execute(sqlUpdate);
                } else {
                    continue;
                }


            }
        }
    }


}
