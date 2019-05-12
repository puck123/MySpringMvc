package com.user.filter;

import com.ruanmou.vip.myspringmvc.util.ArrayUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

/**
 * 软谋教育Java VIP课程
 * <pre>
 *    今日内容:IOC和DI
 * </pre>
 *
 * @author gerry
 * @date 2018-07-09
 */
@WebFilter(filterName = "AutoLoginFilter", urlPatterns = "/*")
public class AutoLoginFilter implements Filter {

    private Properties commUrls;

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {

        // 获取放行列表
        String property = commUrls.getProperty("com.url");
        String[] commmons = property.split(",");

        System.out.println("==========================");
        // 把请求和响应对象转换为Http的
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        // 获取访问的请求路径
        String visitUrl = request.getRequestURI();

        // 判断是否为公共访问资源
        if (ArrayUtil.isNotEmpty(commmons)) {
            for (String url : commmons) {
                if (visitUrl.contains(url)) {
                    chain.doFilter(request, response);
                    return;
                }
            }
        }

        // 判断用户是否登录
        Object account = request.getSession().getAttribute("account");

        // 已经登录
        if (account != null) {
            chain.doFilter(request, response);
        } else {
            // 判断是否勾选了自动登录


            String url = request.getContextPath()+"/login.jsp";
            response.getWriter().print("<script>alert('未登录，请登录后操作');location.href='"+url+"';</script>");
        }
    }

    public void init(FilterConfig config) throws ServletException {
        // 初始化加载内容
        commUrls = new Properties();
        try {
            commUrls.load(this.getClass().getClassLoader().getResourceAsStream("commonurl.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
