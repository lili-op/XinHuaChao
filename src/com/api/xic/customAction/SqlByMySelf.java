package com.api.xic.customAction;

import com.alibaba.fastjson.JSON;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.StaticObj;
import weaver.general.Util;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;
import weaver.interfaces.datasource.DataSource;

/**
 * 门户查询接口
 * */
@Path("/CustomInterface")
public class SqlByMySelf {
    public SqlByMySelf() {
    }

    @POST
    @Path("/getFromInfo")
    public String sendMail(@Context HttpServletRequest request, @Context HttpServletResponse response) throws SQLException {
        String hashroute = request.getParameter("hash");
        HashMap<String, Object> resultMap = new HashMap();
        User user = HrmUserVarify.getUser(request, response);
        int uid = user.getUID();
        int userDepartment = user.getUserDepartment();
        ArrayList<Map<String, Object>> resultList = new ArrayList();
        RecordSet rs = new RecordSet();
        String dbType = rs.getDBType();
        boolean sqlserver = "sqlserver".equals(dbType);
        boolean var12 = rs.executeQuery("select id,ymmc,ysxsx,ymlj,ysxsl from uf_ymccb", new Object[0]);

        while(rs.next()) {
            String row = rs.getString("ysxsx");
            String col = rs.getString("ysxsl");
            if (hashroute.contains(rs.getString("ymlj"))) {
                String typeid = rs.getString("id");
                String sql = "";
                if (sqlserver) {
                    sql = "select a.id,a.mc,a.szym,a.tzdz,a.glzd,a.glz,c.imagefileid,a.gxfw,a.cxyjsql,a.sfwygsj,a.ygsjymc from uf_sqlsearchfrom a LEFT JOIN docimagefile c on cast(c.docid as VARCHAR(MAX))=cast(a.logo as VARCHAR(MAX)) where a.szym='" + typeid + "'  order by a.px asc";
                } else {
                    sql = "select a.id,a.mc,a.szym,a.tzdz,a.glzd,a.glz,c.imagefileid,a.gxfw,a.cxyjsql,a.s fwygsj,a.ygsjymc from uf_sqlsearchfrom a LEFT JOIN docimagefile c on c.docid = a.logo where a.szym='" + typeid + "'  order by a.px asc";
                }

                boolean b = rs.executeQuery(sql, new Object[0]);
                if (b) {
                    Map usub = RoleUtil.getUserSubCom(user);

                    while(true) {
                        HashMap childMap;
                        do {
                            if (!rs.next()) {
                                resultMap.put("errcode", "0");
                                resultMap.put("datas", resultList);
                                (new BaseBean()).writeLog("行的值为" + row);
                                (new BaseBean()).writeLog("列的值为" + col);
                                resultMap.put("row", row);
                                resultMap.put("col", col);
                                resultMap.put("errmsg", "");
                                return JSON.toJSONString(resultMap);
                            }

                            String glzd = rs.getString("glzd");
                            String glz = rs.getString("glz");
                            childMap = new HashMap();
                            childMap.put("id", rs.getString("id"));
                            childMap.put("name", rs.getString("mc"));
                            childMap.put("link", rs.getString("tzdz"));
                            childMap.put("ssym", rs.getString("szym"));
                            String childImg = Util.null2String(rs.getString("imagefileid")).equals("") ? "" : "/weaver/weaver.file.FileDownload?fileid=" + rs.getString("imagefileid");
                            childMap.put("imgurl", childImg);
                            String halfString = HalfUtils.toHalfWidth(rs.getString("cxyjsql"));
                            if (!glzd.equals("") && glzd != null) {
                                halfString = halfString + " where " + glzd + " = " + (glz.equals("0") ? uid : userDepartment);
                            }

                            if (rs.getString("sfwygsj").equals("1")) {
                                DataSource ds = (DataSource)StaticObj.getServiceByFullname("datasource." + rs.getString("ygsjymc"), DataSource.class);
                                Connection conn = ds.getConnection();
                                Statement statement = conn.createStatement();
                                ResultSet ygrs = statement.executeQuery(halfString);
                                if (ygrs.next()) {
                                    childMap.put("count", ygrs.getString("num"));
                                } else {
                                    childMap.put("count", "0");
                                }
                            } else {
                                RecordSet countRs = new RecordSet();
                                countRs.executeQuery(halfString, new Object[0]);
                                countRs.next();
                                childMap.put("count", countRs.getString("num"));
                            }
                        } while(user.getUID() != 1 && !RoleUtil.handleShare(rs.getString("gxfw"), Util.getIntValue((String)usub.get("lever"), -1), new String[]{user.getUID() + "", (String)usub.get("subid"), (String)usub.get("deptid"), (String)usub.get("postid"), (String)usub.get("lever")}));

                        resultList.add(childMap);
                    }
                } else {
                    resultMap.put("errcode", "-1");
                    resultMap.put("errmsg", "");
                    break;
                }
            }
        }

        return JSON.toJSONString(resultMap);
    }
}
