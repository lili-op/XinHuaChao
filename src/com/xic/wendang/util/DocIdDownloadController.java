package com.xic.wendang.util;


import DBstep.iMsgServer2000;
import com.alibaba.fastjson.JSON;
import weaver.conn.ConnStatement;
import weaver.general.BaseBean;
import weaver.general.Util;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipInputStream;
/**
 * 把文档下载（无任务权限限制）
 * ============================================================================
 * @author
 * ----------------------------------------------------------------------------
 * ----------------------------------------------------------------------------
 * @version 版本号 V1.0
 * ----------------------------------------------------------------------------
 * @url :  	   http://localhost/weaver/com.oneWorld.ecology.file.utils.DocIdDownloadController?docid=57
 * 		   或者： http://localhost/weaver/com.oneWorld.ecology.file.utils.DocIdDownloadController?docid=57.jpg
 * 			参数：
 * 				docid:单个文件在流程附件里存的id,  后面的后缀随便一个后缀，主要是以“.”分割，取第一个
 * 				download:不传，或者空，是直接在浏览器上面显示
 * ============================================================================
 */


/**
 * http://oa.xhcicled.com:9981/weaver/com.xic.wendang.util.DocIdDownloadController?docid=57&download=1
 * */
public class DocIdDownloadController extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        //返回数据
        Map<String,Object> bean2 = new HashMap<String, Object>();


        String clientEcoding = "GBK";
        try
        {
            String acceptlanguage = req.getHeader("Accept-Language");
            if(!"".equals(acceptlanguage))
                acceptlanguage = acceptlanguage.toLowerCase();
            if(acceptlanguage.indexOf("zh-tw")>-1||acceptlanguage.indexOf("zh-hk")>-1)
            {
                clientEcoding = "BIG5";
            }
            else
            {
                clientEcoding = "GBK";
            }
        }
        catch(Exception e)
        {

        }


        String docidStr = req.getParameter("docid");
        if(docidStr == null){
            bean2.put("status","false");
            bean2.put("message","未传递附件id");
            //res.setCharacterEncoding("GBK");
            res.setContentType("text/html;charset=GBK");
            PrintWriter out = res.getWriter();
            String retStr = JSON.toJSONString(bean2);
            out.print(retStr);
            out.flush();
            out.close();
            return;
        }

        //转换类型
        int docid = Util.getIntValue(docidStr, -1);//文档

        if(docid <= 0){//转化为int型，防止SQL注入
            bean2.put("status","false");
            bean2.put("message","传入附件id出错");
            //res.setCharacterEncoding("GBK");
            res.setContentType("text/html;charset=GBK");
            PrintWriter out = res.getWriter();
            String retStr = JSON.toJSONString(bean2);
            out.print(retStr);
            out.flush();
            out.close();
            return;
        }
        //String strSql="select docpublishtype from docdetail where id in (select docid from docimagefile where imagefileid="+fileid+") and ishistory <> 1";
        ConnStatement statement = new ConnStatement();
        //RecordSet rs =new RecordSet();

        try {
            //解决问题：一个附件或图片被一篇内部文档和外部新闻同时引用时，外部新闻可能查看不到附件或图片。 update by fanggsh fot TD5478  begin
            //调整为如下：
            //默认需要用户登录信息,不需要登录信息的情形如下：

            //解决问题：一个附件或图片被一篇内部文档和外部新闻同时引用时，外部新闻可能查看不到附件或图片。 update by fanggsh fot TD5478  end
            statement.close();
            String download = Util.null2String(req.getParameter("download"));
            String contenttype = "";
            String filename = "";
            String filerealpath = "";
            String iszip = "";
            String isencrypt = "";

            int byteread;
            byte data[] = new byte[1024];


            String sql = "select i.imagefilename,i.filerealpath,i.iszip,i.isencrypt,i.imagefiletype, i.imagefile from ImageFile i,DocImageFile d where d.imagefileid = i.imagefileid and  d.docid = " + docid;

            boolean isoracle = (statement.getDBType()).equals("mysql");

            String extName = "";

            statement.setStatementSql(sql);
            statement.executeQuery();
            if (statement.next()) {
                filename = Util.null2String(statement.getString("imagefilename"));
                filerealpath = Util.null2String(statement.getString("filerealpath"));
                iszip = Util.null2String(statement.getString("iszip"));
                isencrypt = Util.null2String(statement.getString("isencrypt"));
                if(filename.indexOf(".") > -1){
                    int bx = filename.lastIndexOf(".");
                    extName = filename.substring(bx+1, filename.length());
                }
                InputStream imagefile = null;

                //文件类型查看，如果不是这些类型就下载
                if (download.equals("")){
                    if(filename.toLowerCase().endsWith(".doc")) contenttype = "application/msword";
                    else if(filename.toLowerCase().endsWith(".xls")) contenttype = "application/vnd.ms-excel";
                    else if(filename.toLowerCase().endsWith(".ppt")) contenttype = "application/vnd.ms-powerpoint";
                    else if(filename.toLowerCase().endsWith(".gif")) contenttype = "image/gif";
                    else if(filename.toLowerCase().endsWith(".png")) contenttype = "image/png";
                    else if(filename.toLowerCase().endsWith(".jpg")) contenttype = "image/jpg";
                    else if(filename.toLowerCase().endsWith(".txt")) contenttype = "text/plain";
                    else if(filename.toLowerCase().endsWith(".pdf")) contenttype = "application/pdf";
                    else if(filename.toLowerCase().endsWith(".docx")) contenttype = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
                    else {
                        contenttype = statement.getString("imagefiletype");
                    }
                    try {
                        res.setHeader("content-disposition", "inline; filename=" + new String(filename.getBytes(clientEcoding), "ISO8859_1"));
                    } catch (Exception ecode) {
                    }
                }else {
                    contenttype = "application/octet-stream";
                    try {
                        res.setHeader("content-disposition", "attachment; filename=" + new String(filename.getBytes(clientEcoding), "ISO8859_1"));
                    } catch (Exception ecode) {
                    }
                }

                ZipInputStream zin = null;
                if (filerealpath.equals("")) {         // 旧的文件放在数据库中的方式
                    if (isoracle)
                        imagefile = new BufferedInputStream(statement.getBlobBinary("imagefile"));
                    else
                        imagefile = new BufferedInputStream(statement.getBinaryStream("imagefile"));
                } else {
                    File thefile = new File(filerealpath);
                    if (iszip.equals("1")) {
                        zin = new ZipInputStream(new FileInputStream(thefile));
                        if (zin.getNextEntry() != null) imagefile = new BufferedInputStream(zin);
                    } else{
                        imagefile = new BufferedInputStream(new FileInputStream(thefile));
                    }
                    if(download.equals("1") && ("xls".equalsIgnoreCase(extName) || "doc".equalsIgnoreCase(extName))) {
                        //正文的处理
                        ByteArrayOutputStream bout = null;
                        try {
                            bout = new ByteArrayOutputStream() ;
                            while((byteread = imagefile.read(data)) != -1) {
                                bout.write(data, 0, byteread) ;
                                bout.flush() ;
                            }
                            byte[] fileBody = bout.toByteArray();
                            iMsgServer2000 MsgObj = new iMsgServer2000();
                            MsgObj.MsgFileBody(fileBody);			//将文件信息打包
                            fileBody = MsgObj.ToDocument(MsgObj.MsgFileBody());    //通过iMsgServer200 将pgf文件流转化为普通Office文件流
                            imagefile = new ByteArrayInputStream(fileBody);
                            bout.close();
                        }
                        catch(Exception e) {
                            if(bout!=null) bout.close();
                        }
                    }
                }


                ServletOutputStream out = null;
                try {
                    out = res.getOutputStream();
                    res.setContentType(contenttype);

                    while ((byteread = imagefile.read(data)) != -1) {
                        out.write(data, 0, byteread);
                        out.flush();
                    }
                }
                catch(Exception e) {
                    //do nothing
                }
                finally {
                    if(imagefile!=null) imagefile.close();
                    if(zin!=null) zin.close();
                    if(out!=null) out.flush();
                    if(out!=null) out.close();
                }

            }
        } catch (Exception e) {
            BaseBean basebean = new BaseBean();
            basebean.writeLog(e);
        } //错误处理
        finally {
            statement.close();
        }


//        bean2.put("status","true");
//        bean2.put("massage","获取文件成功");
//
//        res.setCharacterEncoding("UTF-8");
//        res.setContentType("text/html;charset=UTF-8");
//        PrintWriter out = res.getWriter();
//        String retStr = JSON.toJSONString(bean2);
//        out.print(retStr);
//        out.flush();
//        out.close();
    }

}

