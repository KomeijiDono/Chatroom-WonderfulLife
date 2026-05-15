package com.edu.seiryo.wonderfulLife.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.edu.seiryo.wonderfulLife.dao.UserInfoDao;
import com.edu.seiryo.wonderfulLife.pojo.UserInfo;

/**
 * 获取在线用户列表接口
 * GET /getOnlineUsers — 返回当前在线用户的 JSON 数组
 */
@WebServlet("/getOnlineUsers")
public class GetOnlineUsersServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        UserInfoDao userDao = new UserInfoDao();
        List<UserInfo> list = userDao.getOnlineUsers();
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            UserInfo u = list.get(i);
            if (i > 0) json.append(",");
            json.append("{\"account\":\"").append(escape(u.getAccount()))
                .append("\",\"nickname\":\"").append(escape(u.getNickname()))
                .append("\",\"sex\":\"").append(escape(u.getSex()))
                .append("\",\"type\":").append(u.getType()).append("}");
        }
        json.append("]");
        response.getWriter().write(json.toString());
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r");
    }
}
