package com.api.xic.customAction;


import com.alibaba.fastjson.JSON;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;


import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;

@Path("/huaihai")
public class HuaiHai {
    public HuaiHai() {
    }

    @POST
    @Path("/getinfo")
    public String sendMail(@Context HttpServletRequest request, @Context HttpServletResponse response) {
        HashMap<String, Object> resultMap = new HashMap();
        User user = HrmUserVarify.getUser(request, response);
        ArrayList<Map<String, Object>> resultList = new ArrayList();
        RecordSet rs = new RecordSet();
        String dbType = rs.getDBType();
        boolean sqlserver = "sqlserver".equals(dbType);
        String sql = "";
        if (sqlserver) {
            sql = "select a.mc,c.imagefileid,a.gxqx from uf_xzzq a LEFT JOIN docimagefile c on cast(c.docid as VARCHAR(MAX))=cast(a.fj as VARCHAR(MAX)) order by a.px asc";
        } else {
            sql = "select a.mc,c.imagefileid,a.gxqx from uf_xzzq a LEFT JOIN docimagefile c on c.docid = a.fj order by a.px asc";
        }

        boolean b = rs.executeQuery(sql, new Object[0]);
        if (b) {
            Map usub = RoleUtil.getUserSubCom(user);

            while(true) {
                String gxfw;
                HashMap childMap;
                do {
                    if (!rs.next()) {
                        resultMap.put("errcode", "0");
                        resultMap.put("datas", resultList);
                        resultMap.put("errmsg", "");
                        return JSON.toJSONString(resultMap);
                    }

                    gxfw = rs.getString("gxqx");
                    childMap = new HashMap();
                    String childImg = Util.null2String(rs.getString("imagefileid")).equals("") ? "" : "/weaver/weaver.file.FileDownload?download=1&fileid=" + rs.getString("imagefileid");
                    childMap.put("id", rs.getString("id"));
                    childMap.put("mc", rs.getString("mc"));
                    childMap.put("fj", childImg);
                } while(user.getUID() != 1 && !RoleUtil.handleShare(gxfw, Util.getIntValue((String)usub.get("lever"), -1), new String[]{user.getUID() + "", (String)usub.get("subid"), (String)usub.get("deptid"), (String)usub.get("postid"), (String)usub.get("lever")}));

                resultList.add(childMap);
            }
        } else {
            resultMap.put("errcode", "-1");
            resultMap.put("errmsg", "");
            return JSON.toJSONString(resultMap);
        }
    }
}
