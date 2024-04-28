package com.xic.zhouhui.jianmo;

import com.oneWorld.ecology.workflow.utils.ActionUtils;
import weaver.conn.RecordSet;
import weaver.formmode.customjavacode.AbstractModeExpandJavaCodeNew;
import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;
import weaver.soa.workflow.request.RequestInfo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * offer管理点击已到岗自动根据入职岗位获取到对应分数，加入到当前月份当前找朋友的完成积分里面
 */
public class MuBiaoJiFen extends AbstractModeExpandJavaCodeNew {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public Map<String, String> doModeExpand(Map<String, Object> param) {
        log.info("======offer到岗修改对应目标分数===");
        Map<String, String> result = new HashMap<String, String>();
        RecordSet res = new RecordSet();
        String sql;
        String zpfs = "0";
        double wcl = 0.00;
        try {
            //获取到台账里面的数据
            RequestInfo requestInfo = (RequestInfo) param.get("RequestInfo");
            if (requestInfo != null) {
                ActionUtils actionUtils = new ActionUtils();
                //主表字段
                Map<String, String> map = actionUtils.getMainData(requestInfo);
                //申请人（招聘员）
                String sqr = actionUtils.getMapValue(map, "sqr");//招聘员
                //招聘的岗位
                String gw = actionUtils.getMapValue(map, "gw");//岗位

                //当前月份
                String mgy1h = dqrq();//当前月份

                //通过岗位找到对应分数
                sql = "select zpfs from uf_gwjfb where gw="+gw;
                log.info("sql1:"+sql);
                res.execute(sql);
                if(res.next()) {
                    zpfs = res.getString("zpfs");//获取到对应岗位的招聘分数了
                }
                String wyz = "where lx=0 and yf='"+mgy1h+"' and cyr2="+sqr;//唯一值
                log.info("唯一值:"+wyz);

                sql = "update uf_mbgl set wcz+="+zpfs+" "+wyz;//修改完成值
                log.info("sql2:"+sql);
                res.execute(sql);

                sql = "select mbz,wcz from uf_mbgl "+wyz;//查询目标值和完成值
                log.info("sql3:"+sql);
                res.execute(sql);
                if(res.next()){
                    double mbz = Double.parseDouble(res.getString("mbz"));//目标值
                    double wcz = Double.parseDouble(res.getString("wcz"));//完成值
                    //计算完成率
                    wcl = (wcz/mbz)*100;//取两位小数
                }
                String wclresult = String.format("%.2f", wcl);
                sql = "update uf_mbgl set wll="+wclresult+" "+wyz;//修改完成率
                log.info("sql4:"+sql);
                res.execute(sql);
            }
        } catch (Exception e) {
            result.put("errmsg", "自定义出错信息");
            result.put("flag", "false");
        }
        return result;
    }


    public static String dqrq() {
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();

        // 创建日期格式化对象
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        // 使用格式化对象将LocalDate转换为字符串
        String dateString = currentDate.format(formatter);

        return dateString;
    }
}
