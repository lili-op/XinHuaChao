package com.api.xic.jd.tool;

import java.util.HashMap;
import java.util.Map;

//金蝶kis的接口
public class InvokeAPI {
    private static Map<String, String> map = new HashMap<String, String>();
    static {
        //销售出库单
        //批量查询销售出库单
        map.put("Sxscks","https://api.kingdee.com/koas/app007104/api/salesdelivery/list?access_token=");
        //查询销售出库单详情
        map.put("Sxsck","https://api.kingdee.com/koas/app007104/api/salesdelivery/getdetail?access_token=");
        //新增销售出库单
        map.put("Axsck", "https://api.kingdee.com/koas/app007104/api/salesdelivery/create?access_token=");
        //修改销售出库单
        map.put("Uxsck","https://api.kingdee.com/koas/app007104/api/salesdelivery/update?access_token=");
        //删除销售出库单
        map.put("Dxsck","https://api.kingdee.com/koas/app007104/api/salesdelivery/delete?access_token=");


        //批量查询即时库存
        map.put("Sjskcs","https://api.kingdee.com/koas/APP007104/api/ICInventory/List?access_token=");

        //查询物料基础资料详情
        map.put("Swlxx","https://api.kingdee.com/koas/APP006992/api/Material/GetDetail?access_token=");
        //批量查询物料基础资料
        map.put("Swlxxs","https://api.kingdee.com/koas/APP006992/api/Material/List?access_token=");


    }

    public String Sxscks(String key) throws Exception {
        String sUrl = map.get(key);
        return sUrl;
    }

}
