package com.edu.seiryo.wonderfulLife.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.edu.seiryo.wonderfulLife.dao.UserInfoDao;
import com.edu.seiryo.wonderfulLife.pojo.UserInfo;

/**
 * 用户注册接口
 * POST /register — 校验参数合法性，注册新用户
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String account = request.getParameter("account");
        String password = request.getParameter("password");
        String sex = request.getParameter("sex");
        String nickname = request.getParameter("nickname");

        // 参数非空校验
        if (account == null || password == null || sex == null || nickname == null) {
            request.setAttribute("msg", "请填写完整信息");
            request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
            return;
        }

        // 删除空格
        account = account.trim();
        password = password.trim();
        nickname = nickname.trim();

        // 账号密码长度校验（5-12位）
        if (account.length() < 5 || account.length() > 12) {
            request.setAttribute("msg", "账号长度必须在5-12位之间");
            request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
            return;
        }
        if (password.length() < 5 || password.length() > 12) {
            request.setAttribute("msg", "密码长度必须在5-12位之间");
            request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
            return;
        }
        // 昵称长度校验（不超过10个汉字=20字符）
        if (nickname.length() > 20) {
            request.setAttribute("msg", "昵称长度不能超过10个汉字");
            request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
            return;
        }

        // 检查账号是否已被注册
        UserInfoDao userDao = new UserInfoDao();
        if (userDao.isAccountExist(account)) {
            request.setAttribute("msg", "该账号已被注册");
            request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
            return;
        }

        // 执行注册
        UserInfo user = new UserInfo();
        user.setAccount(account);
        user.setPassword(password);
        user.setSex(sex);
        user.setNickname(nickname);
        user.setType(0);
        user.setOnline(0);

        if (userDao.register(user)) {
            request.setAttribute("msg", "注册成功，请登录");
            request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
        } else {
            request.setAttribute("msg", "注册失败，请重试");
            request.getRequestDispatcher("/jsp/register.jsp").forward(request, response);
        }
    }
}
