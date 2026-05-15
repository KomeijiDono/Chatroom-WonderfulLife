package com.edu.seiryo.wonderfulLife.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.edu.seiryo.wonderfulLife.dao.ChatroomConfigDao;
import com.edu.seiryo.wonderfulLife.pojo.ChatroomConfig;
import com.edu.seiryo.wonderfulLife.pojo.UserInfo;

/**
 * 切换聊天室模式接口（管理员专用）
 * GET /toggleChatroom?action=open|close|auto
 *   open  — 手动开启
 *   close — 手动关闭
 *   auto  — 自动模式（按时间段）
 */
@WebServlet("/toggleChatroom")
public class ToggleChatroomServlet extends HttpServlet {
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

        String action = request.getParameter("action");
        ChatroomConfigDao configDao = new ChatroomConfigDao();
        ChatroomConfig config = configDao.getConfig();
        if (config == null) {
            response.getWriter().write("{\"success\":false,\"msg\":\"配置读取失败\"}");
            return;
        }

        // 根据 action 参数设置聊天室模式
        if ("open".equals(action)) {
            config.setManualMode(1);
            config.setIsOpen(1);
        } else if ("close".equals(action)) {
            config.setManualMode(1);
            config.setIsOpen(0);
        } else if ("auto".equals(action)) {
            config.setManualMode(0);
            config.setIsOpen(0);
        } else {
            response.getWriter().write("{\"success\":false,\"msg\":\"未知操作\"}");
            return;
        }

        configDao.updateConfig(config);
        response.getWriter().write("{\"success\":true}");
    }
}
