package com.xic.yx.job;

import weaver.conn.RecordSet;
import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;
import weaver.interfaces.schedule.BaseCronJob;

public class CrmKeHuDizhi extends BaseCronJob {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void execute() {

        RecordSet rs = new RecordSet();
        RecordSet rs1 = new RecordSet();
        String sql = "select id,address1,jdwd from crm_customerinfo";//应收款-业务员体系

        rs.executeQuery(sql);

        while (rs.next()){
            int id = rs.getInt("id");//主键
            String address1 = rs.getString("address1");//主键
            String jdwd = rs.getString("jdwd");//主键
            if(id!=0 && !jdwd.equals("")) {
                String insertsql= String.format("insert into crm_customeraddress" +
                        "(typeid,customerid,address1,jwd)" +
                        "VALUES"+
                        "(%s,%s,'%s','%s')",1,id,address1,jdwd);
                rs1.execute(insertsql);
                System.out.println(insertsql);


            }
        }

    }
}
