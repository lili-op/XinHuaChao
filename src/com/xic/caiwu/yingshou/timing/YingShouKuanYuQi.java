package com.xic.caiwu.yingshou.timing;

import cn.hutool.core.lang.UUID;
import com.oneWorld.ecology.mode.utils.FormmodeUtil;
import com.weaver.general.TimeUtil;
import weaver.common.StringUtil;
import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;
import weaver.interfaces.schedule.BaseCronJob;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 应收款逾期天数根据应收款日期来判断
 */

public class YingShouKuanYuQi extends BaseCronJob {

    private Logger log = LoggerFactory.getLogger(getClass());
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    //当前时间
    Date date = new Date();

    @Override
    public void execute() {
        log.info("开始应收款逾期天数根据应收款日期来判断");
        Map<String, String> map = null;
        List<Map<String, String>> list = new ArrayList<>();
        String modleTable = "uf_ysk";//应收款台账
        FormmodeUtil formmodeUtil = new FormmodeUtil();
        //第一个
        String formmodeid = formmodeUtil.getModeidName4Table(modleTable);
        //第四个
        String nowDate = TimeUtil.getCurrentDateString();
        //第五个
        String nowTime = TimeUtil.getOnlyCurrentTimeString();
        //修改权限
        ModeRightInfo modeRightInfo = new ModeRightInfo();
        try {
            String sql = "";
            String sql1 = "";
            RecordSet rs = new RecordSet();
            RecordSet res1 = new RecordSet();
            //查询出判断逾期总金额大于0的数据
            sql = "select * from uf_ysk where yqzje > 0 and sfxc=0";
            rs.execute(sql);
            while (rs.next()) {
                map = new HashMap<>();
                String id = rs.getString("id");
                String fzr = rs.getString("fzr");//负责人
                String bm = rs.getString("bm");//部门
                String khmc = rs.getString("khmc");//客户id
                String sales = rs.getString("sales");//salesid
                String sjzq = rs.getString("sjzq");//实际账期
                String tpfkrq = rs.getString("tpfkrq");//特批付款日
                String yjfs = rs.getString("yjfs");//月结方式
                String ks = rs.getString("ks");//客诉
                String yskrq = rs.getString("yskrq");//应收款日期
                String yqts = rs.getString("yqts");//逾期天数
                String yqje = rs.getString("yqje");//逾期金额
                String yhkje = rs.getString("yhkje");//已回款金额
                String yqzje = rs.getString("yqzje");//逾期总金额
                String sfxc = rs.getString("sfxc");//是否消除0为否1为是


                map.put("id", id);//唯一值
                map.put("fzr", fzr);//负责人
                map.put("bm", bm);//部门
                map.put("khmc", khmc);//客户id
                map.put("sales", sales);//salesid
                map.put("sjzq", sjzq);//实际账期
                map.put("tpfkrq", tpfkrq);//特批付款日
                map.put("yjfs", yjfs);//月结方式
                map.put("ks", ks);//客诉
                map.put("yskrq", yskrq);//应收款日期
                map.put("yqts", yqts);//逾期天数
                map.put("yqje", yqje);//逾期金额
                map.put("yhkje", yhkje);//已回款金额
                map.put("yqzje", yqzje);//逾期总金额
                map.put("sfxc", sfxc);//是否消除
                list.add(map);
            }

            for (Map<String, String> hamap : list) {
                //第六个
                String uuid = UUID.randomUUID().toString();

                String id = "";
                String ylid = hamap.get("id");//元数据id
                String fzr = hamap.get("fzr");//负责人
                String bm = hamap.get("bm");//部门
                String khmc = hamap.get("khmc");//客户id
                String sales = hamap.get("sales");//salesid
                String sjzq = hamap.get("sjzq");//实际账期
                String tpfkrq = hamap.get("tpfkrq");//特批付款日
                String yjfs = hamap.get("yjfs");//月结方式
                String ks = hamap.get("ks");//客诉
                String yskrq = hamap.get("yskrq");//应收款日期
                String yqts = hamap.get("yqts");//逾期天数
                String yqje = hamap.get("yqje");//逾期金额
                String yqzje = hamap.get("yqzje");//逾期总金额
                String sfxc = hamap.get("sfxc");//是否消除0为否1为是

                //唯一值
                String sqlpj = " and khmc='" + khmc + "' and sales='" + sales + "' and yskrq='" + yskrq + "' and sfxc=0";

                Date date2 = sdf.parse(yskrq);
                //先判断应收款日期到当前日期有多少天
                long diff = date.getTime() - date2.getTime();
                long days = diff / (1000 * 60 * 60 * 24);//相差天数

                if(ylid.equals("294")){
                    System.out.println(days);
                    int i = 0;
                }


                if (days >= 90) {//逾期大于等于90天（90天肯定是从60天叠加上去的）
                    yqts = "3";//逾期天数选项
                    //先通过客户名称-sales-应收款日期，判断有没有90天这个选项，如果没有就要新增，如果有说明当前这一条就是90天的就不用管了
                    sql = "select id from uf_ysk where yqts=" + yqts + sqlpj;
                    rs.execute(sql);
                    if (rs.next()) {
                        id = rs.getString("id");
                    }
                    if (StringUtil.isNull(id)) {//如果为空的话新增数据到90天，只需要吧逾期总金额带入逾期金额
                        sql = "insert into uf_ysk(formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime,modeuuid," +
                                "fzr,bm,khmc,sales,sjzq,tpfkrq,yjfs,ks,yskrq,yqts,yqje,yqzje,sfxc) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        boolean panduan = rs.executeUpdate(sql, new Object[]{
                                formmodeid, 1, 0, nowDate, nowTime, uuid,
                                fzr, bm, khmc, sales, sjzq, tpfkrq, yjfs, ks, yskrq, yqts, yqzje, yqzje, sfxc
                        });
                        if (panduan) {//插入成功后把原来数据消除修改为是
                            sql = "update uf_ysk set sfxc = 1 where id=" + ylid;
                            rs.execute(sql);
                        }

                        sql1 = "select id from " + modleTable + " where modeuuid = ?";
                        res1.executeQuery(sql1, uuid);
                        while (res1.next()) {
                            modeRightInfo.rebuildModeDataShareByEdit(1, Integer.parseInt(formmodeid), Integer.parseInt(res1.getString("id")));
                        }
                    }

                } else if (days >= 60) {//逾期大于等于60天
                    yqts = "2";//逾期天数选项
                    //先通过客户名称-sales-应收款日期，判断有没有60天这个选项，如果没有就要新增，如果有说明当前这一条就是90天的就不用管了
                    sql = "select id from uf_ysk where yqts=" + yqts + sqlpj;
                    rs.execute(sql);
                    if (rs.next()) {
                        id = rs.getString("id");
                    }
                    if (StringUtil.isNull(id)) {//如果为空的话新增数据到60天，只需要吧逾期总金额带入逾期金额
                        sql = "insert into uf_ysk(formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime,modeuuid," +
                                "fzr,bm,khmc,sales,sjzq,tpfkrq,yjfs,ks,yskrq,yqts,yqje,yqzje,sfxc) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        boolean panduan = rs.executeUpdate(sql, new Object[]{
                                formmodeid, 1, 0, nowDate, nowTime, uuid,
                                fzr, bm, khmc, sales, sjzq, tpfkrq, yjfs, ks, yskrq, yqts, yqzje, yqzje, sfxc
                        });
                        if (panduan) {//插入成功后把原来数据消除修改为是ss
                            sql = "update uf_ysk set sfxc = 1 where 1=1" + sqlpj;
                            rs.execute(sql);
                        }

                        sql1 = "select id from " + modleTable + " where modeuuid = ?";
                        res1.executeQuery(sql1, uuid);
                        while (res1.next()) {
                            modeRightInfo.rebuildModeDataShareByEdit(1, Integer.parseInt(formmodeid), Integer.parseInt(res1.getString("id")));
                        }
                    }
                } else if (days >= 30) {//逾期大于等于30天
                    yqts = "1";//逾期天数选项
                    //先通过客户名称-sales-应收款日期，判断有没有大于30天这个选项，如果没有就要新增，如果有说明当前这一条就是90天的就不用管了
                    sql = "select id from uf_ysk where yqts=" + yqts + sqlpj;
                    rs.execute(sql);
                    if (rs.next()) {
                        id = rs.getString("id");
                    }
                    if (StringUtil.isNull(id)) {//如果为空的话新增数据到大于30天，只需要吧逾期总金额带入逾期金额
                        sql = "insert into uf_ysk(formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime,modeuuid," +
                                "fzr,bm,khmc,sales,sjzq,tpfkrq,yjfs,ks,yskrq,yqts,yqje,yqzje,sfxc) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        boolean panduan = rs.executeUpdate(sql, new Object[]{
                                formmodeid, 1, 0, nowDate, nowTime, uuid,
                                fzr, bm, khmc, sales, sjzq, tpfkrq, yjfs, ks, yskrq, yqts, yqzje, yqzje, sfxc
                        });
                        if (panduan) {//插入成功后把原来数据消除修改为是ss
                            sql = "update uf_ysk set sfxc = 1 where 1=1" + sqlpj;
                            rs.execute(sql);
                        }

                        sql1 = "select id from " + modleTable + " where modeuuid = ?";
                        res1.executeQuery(sql1, uuid);
                        while (res1.next()) {
                            modeRightInfo.rebuildModeDataShareByEdit(1, Integer.parseInt(formmodeid), Integer.parseInt(res1.getString("id")));
                        }
                    }
                } else {//逾期小于30天
                    yqts = "0";//逾期天数选项
                    //先通过客户名称-sales-应收款日期，判断有没有小于30天这个选项，如果没有就要新增，如果有说明当前这一条就是90天的就不用管了
                    sql = "select id from uf_ysk where yqts=" + yqts + sqlpj;
                    rs.execute(sql);
                    if (rs.next()) {
                        id = rs.getString("id");
                    }
                    if (StringUtil.isNull(id)) {//如果为空的话新增数据到小于30天，只需要吧逾期总金额带入逾期金额
                        sql = "insert into uf_ysk(formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime,modeuuid," +
                                "fzr,bm,khmc,sales,sjzq,tpfkrq,yjfs,ks,yskrq,yqts,yqje,yqzje,sfxc) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                        boolean panduan = rs.executeUpdate(sql, new Object[]{
                                formmodeid, 1, 0, nowDate, nowTime, uuid,
                                fzr, bm, khmc, sales, sjzq, tpfkrq, yjfs, ks, yskrq, yqts, yqzje, yqzje, sfxc
                        });
                        if (panduan) {//插入成功后把原来数据消除修改为是ss
                            sql = "update uf_ysk set sfxc = 1 where 1=1" + sqlpj;
                            rs.execute(sql);
                        }

                        sql1 = "select id from " + modleTable + " where modeuuid = ?";
                        res1.executeQuery(sql1, uuid);
                        while (res1.next()) {
                            modeRightInfo.rebuildModeDataShareByEdit(1, Integer.parseInt(formmodeid), Integer.parseInt(res1.getString("id")));
                        }
                    }
                }


            }
        } catch (Exception e) {
            log.info("插入周会,报错信息:", e);
        }
    }

    public static String getSaturdayDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 1);
            return simpleDateFormat.format(cal.getTime());
        }
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 5);
        return simpleDateFormat.format(cal.getTime());
    }
}
