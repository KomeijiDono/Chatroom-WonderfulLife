package com.edu.seiryo.wonderfulLife.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.edu.seiryo.wonderfulLife.dao.ChatroomConfigDao;
import com.edu.seiryo.wonderfulLife.dao.UserInfoDao;
import com.edu.seiryo.wonderfulLife.pojo.UserInfo;

/**
 * 用户登录接口
 * POST /login — 验证账号密码，登录成功后将用户信息存入 session
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String account = request.getParameter("account");
        String password = request.getParameter("password");

        // 参数非空校验
        if (account == null || password == null || account.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("msg", "账号和密码不能为空");
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
            return;
        }

        // 查询数据库验证账号密码
        UserInfoDao userDao = new UserInfoDao();
        UserInfo user = userDao.login(account, password);

        if (user == null) {
            request.setAttribute("msg", "账号或密码错误");
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
            return;
        }

        // 普通用户登录时检查聊天室是否开放
        if (user.getType() != 1) {
            ChatroomConfigDao configDao = new ChatroomConfigDao();
            if (!configDao.isChatroomOpen()) {
                request.setAttribute("msg", "聊天室当前未开放（开放时间 09:00-18:00）");
                request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
                return;
            }
        }

        // 登录成功：存入 session 并标记在线
        HttpSession session = request.getSession();
        session.setAttribute("user", user);

        userDao.setOnline(account, 1);

        response.sendRedirect(request.getContextPath() + "/chat.jsp");
    }
}
