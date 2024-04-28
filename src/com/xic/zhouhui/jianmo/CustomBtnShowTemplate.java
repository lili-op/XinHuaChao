package com.xic.zhouhui.jianmo;

import weaver.conn.RecordSet;
import weaver.formmode.interfaces.PopedomCommonAction;
import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 当前日期>=数据的每周六日期不显示编辑按钮
 * */

public class CustomBtnShowTemplate implements PopedomCommonAction {


    private final Logger log = LoggerFactory.getLogger(this.getClass());
    //当前日期
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    //当前时间
    Date date = new Date();
    @Override
    public String getIsDisplayOperation(String modeid, String customid, String uid, String billid, String buttonname) {
        try {
            RecordSet rs = new RecordSet();
            String sql = "select mzlrq from uf_yxzhhyzhub where id=?";
            rs.executeQuery(sql, billid);
            if (rs.next()) {
                String mzlrq = rs.getString("mzlrq");//每周六日期字段
                Date date2 = sdf.parse(mzlrq);
                //2.判断当前日期>=每周六日期字段
                if ((date.after(date2))) {
                    return "false";
                } else {
                    return "true";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("抛出异常:", e);
        }
        return "false";
    }
}
