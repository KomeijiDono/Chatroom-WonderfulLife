package com.edu.seiryo.wonderfulLife.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.edu.seiryo.wonderfulLife.dao.ChatDao;
import com.edu.seiryo.wonderfulLife.pojo.UserInfo;

/**
 * 清除聊天记录接口（管理员专用）
 * GET /clearChat — 删除 t_chat 表中所有消息
 */
@WebServlet("/clearChat")
public class ClearChatServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        // 检查管理员权限
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.getWriter().write("{\"success\":false,\"msg\":\"未登录\"}");
            return;
        }
        UserInfo user = (UserInfo) session.getAttribute("user");
        if (user == null || user.getType() != 1) {
            response.getWriter().write("{\"success\":false,\"msg\":\"无权限\"}");
            return;
        }
        ChatDao chatDao = new ChatDao();
        chatDao.clearAll();
        response.getWriter().write("{\"success\":true}");
    }
}
