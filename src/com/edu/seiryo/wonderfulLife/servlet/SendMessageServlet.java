package com.edu.seiryo.wonderfulLife.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.edu.seiryo.wonderfulLife.dao.ChatDao;
import com.edu.seiryo.wonderfulLife.pojo.Chat;
import com.edu.seiryo.wonderfulLife.pojo.UserInfo;

/**
 * 发送消息接口
 * POST /sendMessage — 接收文本或图片消息，保存到数据库
 */
@WebServlet("/sendMessage")
public class SendMessageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        // 检查登录状态
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.getWriter().write("{\"success\":false,\"msg\":\"未登录\"}");
            return;
        }
        UserInfo user = (UserInfo) session.getAttribute("user");
        if (user == null) {
            response.getWriter().write("{\"success\":false,\"msg\":\"未登录\"}");
            return;
        }

        String info = request.getParameter("info");
        String color = request.getParameter("color");
        String type = request.getParameter("type");

        // 消息内容非空校验
        if (info == null || info.trim().isEmpty()) {
            response.getWriter().write("{\"success\":false,\"msg\":\"消息不能为空\"}");
            return;
        }

        if (color == null || color.isEmpty()) {
            color = "#000000";
        }

        // 如果是图片消息，包装为 HTML img 标签
        String content = info.trim();
        if ("img".equals(type)) {
            content = "<img src='" + content + "' style='max-width:200px;max-height:200px' />";
        }

        Chat chat = new Chat();
        chat.setInfo(content);
        chat.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        chat.setSender(user.getAccount());
        chat.setColor(color);

        ChatDao chatDao = new ChatDao();
        chatDao.sendMessage(chat);

        response.getWriter().write("{\"success\":true}");
    }
}
