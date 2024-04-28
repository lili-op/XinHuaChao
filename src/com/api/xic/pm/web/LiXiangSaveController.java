package com.api.xic.pm.web;


import com.xic.pm.workflow.bean.CommonResult;
import weaver.conn.RecordSet;
import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * @author tangaofeng
 * @Description 科研项目立项流程在FAE工程师节点填写预计开始日期点击保存后回写到台账并且修改方案状态为开始
 * @since 2022-9-16
 */
@Path("/xhc/pm/lc")
public class LiXiangSaveController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @POST
    @Path("/BaoCun")
    @Produces({MediaType.APPLICATION_JSON})
    public CommonResult BaoCun(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        log.info("开始执行预计开始日期保存");
        String xmbh = request.getParameter("xmbh");//项目编号
        String yjkssj = request.getParameter("yjkssj");//预计开始时间
        log.info("获取到的值为:"+xmbh+"--"+yjkssj);
        try {
            RecordSet res = new RecordSet();
            String sql = "update uf_projectinfo set yjksrq = '" + yjkssj + "',fazt=1 where proCode='" + xmbh + "'";
            log.info("sql为:"+sql);
            boolean panduan = res.execute(sql);
            if (panduan) {
                return new CommonResult(200, "", "");//true
            } else {
                return new CommonResult(400, "开始失败", "");//fasle

            }
        } catch (Exception e) {
            return new CommonResult(500, "异常", e);//false
        }

    }


}
