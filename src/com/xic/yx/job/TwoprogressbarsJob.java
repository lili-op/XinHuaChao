package com.xic.yx.job;


import com.xic.util.DateUtil;
import weaver.conn.RecordSet;
import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;
import weaver.interfaces.schedule.BaseCronJob;

/**
 * 营销
 * 定时刷新应收款，库存的倒计时进度条、回款进度条数据
 */

public class TwoprogressbarsJob extends BaseCronJob {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void execute() {


    }
}
