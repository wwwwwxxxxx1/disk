package com.servlet; /**
 * @Author wuxin
 * @Date 2023/6/27 20:42
 * @Description ${description}
 * @Version ${version}
 */

import com.beans.DiskFileInfo;
import com.beans.UserInfo;
import com.dao.HdfsDao;
import com.dao.impl.HdfsDaoImpl;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/MapReduceServlet")
public class MapReduceServlet extends HttpServlet {

    private HdfsDao hdfsDao = new HdfsDaoImpl();

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String flag = request.getParameter("flag");

        if("searchFilesForWordCount".equals(flag)){
            searchFilesForWordCount(request,response);
        }
        else if("removeRepeat".equals(flag)){
            removeRepeat(request,response);
        }
        else if("searchFilesForSort".equals(flag)){
            searchFilesForSort(request,response);
        }
        else if("searchFilesForPartSort".equals(flag)){
            searchFilesForPartSort(request,response);
        }
    }
    private void searchFilesForWordCount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, ServletException, IOException {
        UserInfo user=(UserInfo)request.getSession().getAttribute("session_user");

        String fileType=request.getParameter("type");
        List<DiskFileInfo> hdfsFileList =hdfsDao.getFileListByType(user.getUserName(),fileType);

        request.setAttribute("hdfsFileList", hdfsFileList);
        request.getRequestDispatcher("/mapreduce/file-list-wordcount.jsp").forward(request, response);
    }
    private void removeRepeat(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserInfo user=(UserInfo)request.getSession().getAttribute("session_user");

        String fileType=request.getParameter("type");
        List<DiskFileInfo> hdfsFileList =hdfsDao.getFileListByType(user.getUserName(),fileType);

        request.setAttribute("hdfsFileList", hdfsFileList);
        request.getRequestDispatcher("/mapreduce/file-list-removerepeat.jsp").forward(request, response);

    }
    private void searchFilesForSort(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserInfo user=(UserInfo)request.getSession().getAttribute("session_user");

        String fileType=request.getParameter("type");
        List<DiskFileInfo> hdfsFileList =hdfsDao.getFileListByType(user.getUserName(),fileType);

        request.setAttribute("hdfsFileList", hdfsFileList);
        request.getRequestDispatcher("/mapreduce/file-list-sort.jsp").forward(request, response);
    }
    private void searchFilesForPartSort(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserInfo user=(UserInfo)request.getSession().getAttribute("session_user");

        String fileType=request.getParameter("type");
        List<DiskFileInfo> hdfsFileList =hdfsDao.getFileListByType(user.getUserName(),fileType);

        request.setAttribute("hdfsFileList", hdfsFileList);
        request.getRequestDispatcher("/mapreduce/file-list-partsort.jsp").forward(request, response);
    }
}
