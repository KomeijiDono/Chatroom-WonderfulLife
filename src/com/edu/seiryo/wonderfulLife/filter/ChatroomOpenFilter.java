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

import com.edu.seiryo.wonderfulLife.dao.ChatroomConfigDao;
import com.edu.seiryo.wonderfulLife.dao.UserInfoDao;
import com.edu.seiryo.wonderfulLife.pojo.UserInfo;

/**
 * 聊天室开放状态过滤器
 * 管理员不受限制；普通用户在聊天室关闭时被强制下线并跳转到登录页
 * 支持 AJAX 请求和普通请求两种响应方式
 */
public class ChatroomOpenFilter implements Filter {
    public void init(FilterConfig filterConfig) {}

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        // 管理员不受开放时间限制，直接放行
        UserInfo user = (session != null) ? (UserInfo) session.getAttribute("user") : null;
        if (user != null && user.getType() == 1) {
            chain.doFilter(request, response);
            return;
        }

        ChatroomConfigDao configDao = new ChatroomConfigDao();
        if (!configDao.isChatroomOpen()) {
            // 聊天室已关闭：将所有在线用户强制离线并销毁会话
            UserInfoDao userDao = new UserInfoDao();
            userDao.setOfflineAll();

            if (session != null) {
                session.invalidate();
            }

            // AJAX 请求返回 JSON，普通请求转发到登录页
            String ajaxHeader = req.getHeader("X-Requested-With");
            if ("XMLHttpRequest".equals(ajaxHeader)) {
                resp.setContentType("application/json;charset=utf-8");
                resp.getWriter().write("{\"success\":false,\"msg\":\"聊天室已关闭\",\"redirect\":\"" 
                    + req.getContextPath() + "/jsp/login.jsp\"}");
                return;
            }

            req.setAttribute("msg", "聊天室已关闭，所有用户已下线（开放时间 09:00-18:00）");
            req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
            return;
        }
        chain.doFilter(request, response);
    }

    public void destroy() {}
}
