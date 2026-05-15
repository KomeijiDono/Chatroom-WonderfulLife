package com.edu.seiryo.wonderfulLife.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录检查过滤器
 * 拦截对聊天页面的访问，未登录用户跳转到登录页
 * 白名单：login.jsp 和 register.jsp 无需登录
 */
public class CheckLoginFilter implements Filter {
    public void init(FilterConfig filterConfig) {}

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String path = req.getRequestURI().substring(req.getContextPath().length());

        // 登录页和注册页放行，无需登录即可访问
        if (path.equals("/jsp/login.jsp") || path.equals("/jsp/register.jsp")) {
            chain.doFilter(request, response);
            return;
        }

        // 检查 session 中是否有 user 属性来判断是否已登录
        boolean loggedIn = (session != null && session.getAttribute("user") != null);
        if (!loggedIn) {
            req.setAttribute("msg", "您尚未登录，请先登录");
            req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
            return;
        }
        chain.doFilter(request, response);
    }

    public void destroy() {}
}
