package com.edu.seiryo.wonderfulLife.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.edu.seiryo.wonderfulLife.dao.ChatroomConfigDao;
import com.edu.seiryo.wonderfulLife.pojo.ChatroomConfig;

/**
 * 查询聊天室状态接口
 * GET /checkChatroomStatus — 返回聊天室当前开放状态、模式、时间段等信息
 */
@WebServlet("/checkChatroomStatus")
public class CheckChatroomStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=utf-8");
        ChatroomConfigDao configDao = new ChatroomConfigDao();
        ChatroomConfig config = configDao.getConfig();
        boolean isOpen = configDao.isChatroomOpen();
        boolean isInAutoPeriod = configDao.isInAutoOpenPeriod();

        StringBuilder json = new StringBuilder("{");
        json.append("\"isOpen\":").append(isOpen);
        json.append(",\"isInAutoPeriod\":").append(isInAutoPeriod);
        if (config != null) {
            json.append(",\"manualMode\":").append(config.getManualMode());
            json.append(",\"openTime\":\"").append(config.getOpenTime()).append("\"");
            json.append(",\"closeTime\":\"").append(config.getCloseTime()).append("\"");
        }
        json.append("}");
        response.getWriter().write(json.toString());
    }
}
