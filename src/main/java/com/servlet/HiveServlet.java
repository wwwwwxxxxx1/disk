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

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "HiveServlet", value = "/HiveServlet")
public class HiveServlet extends HttpServlet {
    private HdfsDao hdfsDao = new HdfsDaoImpl();

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String flag = request.getParameter("flag");
        if ("searchLogFiles".equals(flag)) {
            searchLogFiles(request, response);
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

}
