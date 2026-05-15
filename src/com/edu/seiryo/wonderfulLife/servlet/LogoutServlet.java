package com.edu.seiryo.wonderfulLife.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.edu.seiryo.wonderfulLife.dao.UserInfoDao;
import com.edu.seiryo.wonderfulLife.pojo.UserInfo;

/**
 * 退出登录接口
 * GET /logout — 清除在线状态、销毁 session、跳转到登录页
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 将用户状态设为离线并销毁 session
        HttpSession session = request.getSession(false);
        if (session != null) {
            UserInfo user = (UserInfo) session.getAttribute("user");
            if (user != null) {
                UserInfoDao userDao = new UserInfoDao();
                userDao.setOnline(user.getAccount(), 0);
            }
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
    }
}
