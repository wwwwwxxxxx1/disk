package com.servlet; /**
 * @Author wuxin
 * @Date 2023/6/30 16:23
 * @Description ${description}
 * @Version ${version}
 */

import com.beans.DiskFileInfo;
import com.beans.UserInfo;
import com.dao.HdfsDao;
import com.dao.impl.HdfsDaoImpl;
import com.util.StrUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "HiveServlet", value = "/HiveServlet")
public class HiveServlet extends HttpServlet {
    private HdfsDao hdfsDao = new HdfsDaoImpl();

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String flag = request.getParameter("flag");
        if ("searchLogFiles".equals(flag)) {
            searchLogFiles(request, response);
        }
        else if ("manageSubFiles".equals(flag)) {
            manageSubFiles(request, response);
        }
        else if ("delete".equals(flag)) {
            delete(request, response);
        }

    }
    private void searchLogFiles(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserInfo user=(UserInfo) request.getSession().getAttribute("session_user");

        //约定好,日志文件必须放在每个用户的 log目录下
        String parent=user.getUserName()+"/log";

        DiskFileInfo[] hdfsFileList = hdfsDao.getSubFileList(parent);
        request.setAttribute("hdfsFileList", hdfsFileList);

        request.getRequestDispatcher("/hive/list-log.jsp").forward(request, response);
    }
    private void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fileName = request.getParameter("fileName");
        String parent = fileName.substring(0, fileName.lastIndexOf("/") + 1);

        if (hdfsDao.deleteFile(fileName) == true) {
            request.setAttribute("msg", "删除成功");
            request.setAttribute("parent", parent);
            this.manageSubFiles(request,response);
        }
    }
    private void manageSubFiles(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        String parent=request.getParameter("parent");   //admin/javatools/aaa/bbb/ccc
        if(StrUtil.IsNullOrEmpty(parent)){

            //如果用户是做文上传传后调有用的 manageSubFiles这个方法,这个 parent是不能通过 request.getParameter() 传过来的  所以这里添了这个
            parent=(String) request.getAttribute("parent");
            if(StrUtil.IsNullOrEmpty(parent)){
                UserInfo user=(UserInfo)request.getSession().getAttribute("session_user");
                parent=user.getUserName();
            }
        }


        DiskFileInfo[] hdfsFileList = hdfsDao.getSubFileList(parent);
        request.setAttribute("hdfsFileList", hdfsFileList);


        //以下处理的是前台上面的 导航 类似:  javatools  > aaa  > bbb  > ccc 的呈现
        List<String> urlList=new ArrayList<String>();
        urlList.add(parent);

        //处理完后,是  admin, admin/java, admin/java/lesson7 这样
        while(parent.lastIndexOf("/")!=-1){
            parent=	parent.substring(0,parent.lastIndexOf("/"));
            urlList.add(0,parent );
        }

        urlList.remove(0);  //用户家目录没有必要在前台显示,所以排除

        for (int i=0;i<urlList.size();i++) {
            String str=urlList.get(i);
            urlList.set(i,str+"_"+str.substring(str.lastIndexOf("/")+1));
        }

        request.setAttribute("urlList", urlList);
        request.getRequestDispatcher("/hive/list-log.jsp").forward(request, response);
    }

}
