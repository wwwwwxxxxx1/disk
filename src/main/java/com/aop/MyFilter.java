package com.aop;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Author wuxin
 * @Date 2023/6/25 21:25
 * @Description
 * @Version
 */
public class MyFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {


    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req=(HttpServletRequest) request;
        String uri=req.getRequestURI();
        String contextPath=req.getContextPath();  //linda-disk

        //以下这些路径不用过滤
        String [] excludePaths= {
                contextPath+"/images",
                contextPath+"/css",
                contextPath+"/js",
                contextPath+"/login.jsp",
                contextPath+"/register.jsp",
                contextPath+"/UserServlet"
        };

        //不需要拦的请求,直接放行
        for(String path:excludePaths) {
            if(uri.startsWith(path)) {
                chain.doFilter(request, response);
                return;
            }
        }

    }

    @Override
    public void destroy() {

    }
}
