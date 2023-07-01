package com.servlet; /**
 * @Author wuxin
 * @Date 2023/7/1 14:29
 * @Description ${description}
 * @Version ${version}
 */

import com.beans.LogGroupInfo;
import com.beans.LogInfo;
import com.beans.UserInfo;
import com.dao.HiveDao;
import com.dao.impl.HiveDaoImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "Log_20_HiveServlet", value = "/Log_20_HiveServlet")
public class Log_20_HiveServlet extends HttpServlet {
    private HiveDao hiveDao=new HiveDaoImpl();

    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserInfo user=(UserInfo)request.getSession().getAttribute("session_user");

        List<LogGroupInfo> logList= hiveDao.get_ershi(user.getUserName());
        request.setAttribute("logList", logList);
        request.getRequestDispatcher("/hive/log_20_table.jsp").forward(request, response);
    }
}
