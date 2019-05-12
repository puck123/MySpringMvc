package com.bk.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 软谋教育Java VIP课程
 * <pre>
 *    今日内容:过滤器和监听器
 * </pre>
 *
 * @author gerry
 * @date 2018-06-28
 */
/*@WebFilter(filterName = "CharacterEncodingFilter", urlPatterns = "/*",
        initParams = {
        @WebInitParam(name="encode" , value = "UTF-8")
})*/
public class CharacterEncodingFilter implements Filter {

    private String encode;

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        // 把请求和响应对象强制转换为Http的请求和响应对象
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        // 设置编码格式
        request.setCharacterEncoding(encode);
        response.setContentType("text/html;charset="+encode);

        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {
        // 读取配置中的编码格式
        this.encode = config.getInitParameter("encode");
    }

}
