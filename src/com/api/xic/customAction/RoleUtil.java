package com.api.xic.customAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.hrm.User;


public class RoleUtil {
    public RoleUtil() {
    }

    public static Map<String, String> getUserSubCom(User user) {
        Map<String, String> result = new HashMap();
        result.put("subid", String.valueOf(user.getUserSubCompany1()));
        result.put("deptid", String.valueOf(user.getUserDepartment()));
        result.put("postid", user.getJobtitle());
        result.put("lever", user.getSeclevel());
        return result;
    }

    public static boolean handleShare(String shares, int lever, String... datas) {
        if ("".equals(shares)) {
            return true;
        } else {
            ArrayList<String> tablesubs = Util.TokenizerString(shares, "~");
            Iterator var4 = tablesubs.iterator();

            boolean falg;
            do {
                if (!var4.hasNext()) {
                    return false;
                }

                String share = (String)var4.next();
                ArrayList<String> slist = Util.TokenizerString(share, "_");
                if (slist.size() < 4) {
                    return false;
                }

                falg = false;
                String type = (String)slist.get(0);
                boolean min = false;
                boolean max = false;
                byte var12 = -1;
                switch(type.hashCode()) {
                    case 49:
                        if (type.equals("1")) {
                            var12 = 0;
                        }
                        break;
                    case 50:
                        if (type.equals("2")) {
                            var12 = 1;
                        }
                        break;
                    case 51:
                        if (type.equals("3")) {
                            var12 = 2;
                        }
                        break;
                    case 52:
                        if (type.equals("4")) {
                            var12 = 3;
                        }
                        break;
                    case 53:
                        if (type.equals("5")) {
                            var12 = 4;
                        }
                        break;
                    case 54:
                        if (type.equals("6")) {
                            var12 = 5;
                        }
                }

                switch(var12) {
                    case 0:
                        falg = sharePowerByUser(datas[0], (String)slist.get(1));
                        break;
                    case 1:
                        falg = sharePower(datas[1], lever, slist);
                        break;
                    case 2:
                        falg = sharePower(datas[2], lever, slist);
                        break;
                    case 3:
                        falg = sharePowerByRole(slist, datas[0], datas[1], datas[2], datas[4]);
                        break;
                    case 4:
                        falg = sharePowerByLever(slist, lever);
                        break;
                    case 5:
                        falg = sharePowerByPost((String)slist.get(1), datas[4]);
                        break;
                    default:
                        falg = true;
                }
            } while(!falg);

            return falg;
        }
    }

    public static boolean sharePowerByPost(String shaerPost, String post) {
        return shaerPost.equals(post);
    }

    public static boolean sharePowerByLever(ArrayList<String> slist, int lever) {
        if (lever == -1) {
            return true;
        } else {
            String _slpit = (String)slist.get(3);
            ArrayList<String> _slpits = Util.TokenizerString(_slpit, "|@|");
            int min;
            int max;
            if (_slpit.indexOf("|@|") > 0) {
                min = Util.getIntValue((String)_slpits.get(0), 0);
                max = Util.getIntValue((String)_slpits.get(1), 0);
            } else {
                String[] levers = ((String)_slpits.get(0)).split("-");
                min = Util.getIntValue(levers[0], 0) * -1;
                max = Util.getIntValue(levers[1]);
            }

            return lever >= min && lever <= max;
        }
    }

    public static boolean sharePower(String id, int lever, ArrayList<String> slist) {
        String _slpit = (String)slist.get(3);
        ArrayList<String> _slpits = Util.TokenizerString(_slpit, "|@|");
        int min;
        int max;
        if (_slpit.indexOf("|@|") > 0) {
            min = Util.getIntValue((String)_slpits.get(0), 0);
            max = Util.getIntValue((String)_slpits.get(1), 0);
        } else {
            String[] levers = ((String)_slpits.get(0)).split("-");
            min = Util.getIntValue(levers[0], 0) * -1;
            max = Util.getIntValue(levers[1]);
        }

        return sharePowerBySubId(id, (String)slist.get(1), lever, min, max);
    }

    public static boolean sharePowerByUser(String uid, String shareUser) {
        return shareUser.equals(uid);
    }

    public static boolean sharePowerBySubId(String id, String shareid, int lever, int minLever, int maxLever) {
        if (!id.equals(shareid)) {
            return false;
        } else if (lever != -1 && lever >= minLever && lever <= maxLever) {
            return true;
        } else {
            return lever == -1;
        }
    }

    public static boolean sharePowerByRole(ArrayList<String> slist, String uid, String subId, String deptId, String postId) {
        int id = Util.getIntValue((String)slist.get(1), -1);
        if (id < 0) {
            return false;
        } else {
            String _slpit = (String)slist.get(3);
            ArrayList<String> _slpits = Util.TokenizerString(_slpit, "|@|");
            int min;
            int max;
            if (_slpit.indexOf("|@|") > 0) {
                min = Util.getIntValue((String)_slpits.get(0), 0);
                max = Util.getIntValue((String)_slpits.get(1), 0);
            } else {
                String[] levers = ((String)_slpits.get(0)).split("-");
                min = Util.getIntValue(levers[0], 0) * -1;
                max = Util.getIntValue(levers[1]);
            }

            String sql = "select resourcetype,roleid,resourceid,seclevelfrom,seclevelto from hrmrolemembers where roleid = ?";
            RecordSet rs = new RecordSet();
            rs.executeQuery(sql, new Object[]{id});

            boolean falg;
            do {
                if (!rs.next()) {
                    return false;
                }

                int type = rs.getInt("resourcetype");
                int rolemin = rs.getInt("seclevelfrom");
                int rolemax = rs.getInt("seclevelto");
                String resourceid = rs.getString("resourceid");
                falg = false;
                switch(type) {
                    case 1:
                        falg = handeRole(resourceid, uid, min, max, rolemin, rolemax);
                        break;
                    case 2:
                        falg = handeRole(resourceid, subId, min, max, rolemin, rolemax);
                        break;
                    case 3:
                        falg = handeRole(resourceid, deptId, min, max, rolemin, rolemax);
                        break;
                    case 4:
                    case 6:
                    case 7:
                    default:
                        falg = false;
                        break;
                    case 5:
                        falg = handeRole(resourceid, postId, min, max, rolemin, rolemax);
                        break;
                    case 8:
                        falg = true;
                }
            } while(!falg);

            return falg;
        }
    }

    public static boolean handeRole(String roleId, String id, int min, int max, int rolemin, int rolemax) {
        if (!roleId.equals(id)) {
            return false;
        } else if (rolemin == -1 && rolemax == -1) {
            return true;
        } else {
            return min <= rolemax && max >= rolemin && min <= max;
        }
    }
}

