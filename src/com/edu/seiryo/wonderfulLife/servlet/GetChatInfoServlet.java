package com.edu.seiryo.wonderfulLife.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.edu.seiryo.wonderfulLife.dao.ChatDao;
import com.edu.seiryo.wonderfulLife.pojo.Chat;

/**
 * 获取聊天消息接口
 * GET /getChatInfo — 支持全量拉取和增量拉取（通过 lastId 参数）
 */
@WebServlet("/getChatInfo")
public class GetChatInfoServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        ChatDao chatDao = new ChatDao();

        // 有 lastId 则增量拉取，否则全量拉取
        String lastIdStr = request.getParameter("lastId");
        List<Chat> messages;
        int maxId;
        int dataVersion;

        if (lastIdStr != null && !lastIdStr.isEmpty()) {
            int lastId = Integer.parseInt(lastIdStr);
            messages = chatDao.getMessagesAfter(lastId);
        } else {
            messages = chatDao.getMessages();
        }
        maxId = chatDao.getMaxId();
        dataVersion = ChatDao.getDataVersion();

        StringBuilder json = new StringBuilder("{\"maxId\":");
        json.append(maxId).append(",\"dataVersion\":").append(dataVersion).append(",\"messages\":[");
        for (int i = 0; i < messages.size(); i++) {
            Chat c = messages.get(i);
            if (i > 0) json.append(",");
            json.append("{\"id\":").append(c.getId())
                .append(",\"info\":\"").append(escape(c.getInfo()))
                .append("\",\"time\":\"").append(escape(c.getTime()))
                .append("\",\"sender\":\"").append(escape(c.getSender()))
                .append("\",\"nickname\":\"").append(escape(c.getNickname()))
                .append("\",\"color\":\"").append(escape(c.getColor())).append("\"}");
        }
        json.append("]}");
        response.getWriter().write(json.toString());
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r");
    }
}
