package com.servlet; /**
 * @Author wuxin
 * @Date 2023/6/30 17:07
 * @Description ${description}
 * @Version ${version}
 */

import com.beans.LogGroupInfo;
import com.beans.UserInfo;
import com.dao.HiveDao;
import com.dao.impl.HiveDaoImpl;
import net.sf.json.JSONArray;


import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "Hive_LogGropyServlet", value = "/Hive_LogGropyServlet")
public class Hive_LogGropyServlet extends HttpServlet {
    private HiveDao hiveDao =new HiveDaoImpl();
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserInfo user=(UserInfo) request.getSession().getAttribute("session_user");
        List<LogGroupInfo> logGroupList =hiveDao.getLogGroupList(user.getUserName());

        for(LogGroupInfo log:logGroupList){
            System.out.println(log);
        }

        //把数据以json格式的方式传给页面
        JSONArray jsonObj=JSONArray.fromObject(logGroupList);
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().print(jsonObj);
    }
}
