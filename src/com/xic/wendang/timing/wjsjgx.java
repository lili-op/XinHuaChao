package com.xic.wendang.timing;

import cn.hutool.core.lang.UUID;
import com.oneWorld.ecology.mode.utils.FormmodeUtil;
import com.weaver.general.TimeUtil;
import weaver.common.StringUtil;
import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;
import weaver.interfaces.schedule.BaseCronJob;

import java.util.*;

/**
 * 定时每天把文档数据插入更新到台账
 */

public class wjsjgx extends BaseCronJob {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void execute() {
        Map<String, String> map = new HashMap<>();
        log.info("开始同步文档数据插入台账");
        String modleTable = "uf_wjgl";
        FormmodeUtil formmodeUtil = new FormmodeUtil();
        //第一个
        String formmodeid = formmodeUtil.getModeidName4Table(modleTable);
        //第四个
        String nowDate = TimeUtil.getCurrentDateString();
        //第五个
        String nowTime = TimeUtil.getOnlyCurrentTimeString();
        //修改权限
        ModeRightInfo modeRightInfo = new ModeRightInfo();

        String paichu = "167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190" +
                ",191,192,193,194,195,196,197,198,199,200,201,202,203,206,204,205,208,261,291,297,376,207,84,85,86,87,94,78,79,80,81,82,83,152" +
                ",153,154,155,156,4,5,6,302,303,304,305,306,307,308,309";
        String sql = "";
        String insertsql = "";
        String sql1 = "";
        String maxsql = "";
        String sql1cs = "";
        RecordSet rs = new RecordSet();
        RecordSet rscr = new RecordSet();
        RecordSet rs1 = new RecordSet();
        RecordSet rs2 = new RecordSet();
        try {
            sql = "select a.id,a.docid,a.imagefilename,b.seccategory from DOCIMAGEFILE a left join docdetail b on a.docid = b.id where b.docstatus = 1" +
                    " and seccategory not in (" + paichu + ")";
            rs.execute(sql);
            while (rs.next()) {
                //查询docid有没有，如果没有就插入主文档数据和明细权限数据，如果有就不用插入主文档信息，需要修改对应明细权限（明细权限先删除再新增）
                String docid = rs.getString("docid");//文件实际id

                sql1cs = "select id from uf_wjgl where wjxxxx = '" + docid + "'";
                rs1.execute(sql1cs);
                String ide = "";
                if (rs1.next()) {
                    ide = rs1.getString("id");
                }

                if (ide == null || ide.equals("")) {//没有重复
                    //第六个
                    String uuid = UUID.randomUUID().toString();
                    String imagefilename = rs.getString("imagefilename");//文件名称
                    String seccategory = rs.getString("seccategory");//所在目录id

                    String wjlj = wenbenshuju(seccategory);
                    insertsql = "insert into uf_wjgl(formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime,modeuuid," +
                            "wjmc,wjxxxx,szmlid,wjlj) values(?,?,?,?,?,?,?,?,?,?)";
                    rscr.executeUpdate(insertsql, new Object[]{
                            formmodeid, 1, 0, nowDate, nowTime, uuid, imagefilename, docid, seccategory, wjlj
                    });


                    sql1 = "select id from uf_wjgl where modeuuid = ?";
                    rs1.executeQuery(sql1, uuid);
                    while (rs1.next()) {
                        modeRightInfo.rebuildModeDataShareByEdit(1, Integer.parseInt(formmodeid), Integer.parseInt(rs1.getString("id")));
                    }

                    //查询最大的
                    String maxzuiid = "";
                    maxsql = "select max(id) as mx from uf_wjgl";
                    rs2.execute(maxsql);
                    if (rs2.next()) {
                        maxzuiid = rs2.getString("mx");
                    }

                    //没有重复的插入主表和明细表
                    map.put("0," + docid, maxzuiid);
                } else {//有重复
                    map.put("1," + docid, ide);
                }
            }
        } catch (Exception e) {
            log.info("插入文件,报错信息:", e);
        }


        //通过docid查找权限插入明细
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String[] keyshuzu = entry.getKey().split(","); // docid

            String key = keyshuzu[1];//docid
            String value = entry.getValue(); // 主表id

            if ("1".equals(keyshuzu[0])) {//有重复清空再插入
                sql = "delete from uf_wjgl_dt1 where mainid=" + value;
                rs1.execute(sql);
            }


            sql = "select docid,issecdefaultshare,sharetype,userid,subcompanyid,departmentid,roleid,foralluser," +
                    "crmid,jobids,seclevel,seclevelmax,sharelevel,downloadlevel" +
                    " from docshare where docid = " + key;
            rs.execute(sql);
            while (rs.next()) {
                String issecdefaultshare = rs.getString("issecdefaultshare");//是否默认共享
                String sharetype = rs.getString("sharetype");//对象类型
                String userid = rs.getString("userid");//用户id
                String subcompanyid = rs.getString("subcompanyid");//分部id
                String departmentid = rs.getString("departmentid");//部门id
                String roleid = rs.getString("roleid");//角色id
                String foralluser = rs.getString("foralluser");//所有人
                String crmid = rs.getString("crmid");//客户id
                String jobids = rs.getString("jobids");//岗位id


               if("94".equals(jobids)){
                   continue;
               }


                String seclevel = rs.getString("seclevel");//安全级别最小值
                String seclevelmax = rs.getString("seclevelmax");//安全级别最大值
                String sharelevel = rs.getString("sharelevel");//权限
                String downloadlevel = rs.getString("downloadlevel");//下载级别

                if (StringUtil.isNotNull(issecdefaultshare)) {//不为空就是默认共享
                    issecdefaultshare = "0";
                } else {
                    issecdefaultshare = "1";
                }
                switch (sharetype) {
                    case "1":
                        sharetype = "0";
                        break;
                    case "2":
                        sharetype = "1";
                        break;
                    case "3":
                        sharetype = "2";
                        break;
                    case "4":
                        sharetype = "3";
                        break;
                    case "5":
                        sharetype = "4";
                        break;
                    case "80":
                        sharetype = "5";
                        break;
                    case "81":
                        sharetype = "6";
                        break;
                    case "84":
                        sharetype = "7";
                        break;
                    case "85":
                        sharetype = "8";
                        break;
                    case "86":
                        sharetype = "10";
                        break;
                    case "10":
                        sharetype = "11";
                        break;
                }
                String aqjb = seclevel + "-" + seclevelmax;

                sql1 = "insert into uf_wjgl_dt1(mainid,gxlx,dxlx,yh,fb,bm,js,syr,kh,gw,aqjb,qxs,xzqx) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
                rs1.executeUpdate(sql1, new Object[]{
                        value, issecdefaultshare, sharetype, userid, subcompanyid, departmentid
                        , roleid, foralluser, crmid, jobids, aqjb, sharelevel, downloadlevel
                });
            }
        }

    }


    //文件全路径
    public String wenbenshuju(String muluid) {

        String mlmc = "";
        RecordSet rs = new RecordSet();
        if ("1".equals(muluid) || "0".equals(muluid)) {
            return mlmc;
        }
        while (true) {
            String sql = "select categoryname,parentid from DocSecCategory where id = " + muluid;
            rs.execute(sql);
            if (rs.next()) {
                String categoryname = rs.getString("categoryname");//目录名称
                String parentid = rs.getString("parentid");//上级目录id
                mlmc += categoryname + "/";
                if (parentid.equals("0")) {
                    break;
                }
                muluid = parentid;
            } else {
                break;
            }
        }
        mlmc = mlmc.substring(0, mlmc.length() - 1);
        return mlmc;
    }
}
