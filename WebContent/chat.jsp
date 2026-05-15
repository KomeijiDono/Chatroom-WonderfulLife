<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.edu.seiryo.wonderfulLife.pojo.UserInfo" %>
<%-- 聊天室主页面：采用 frameset 分成上中下三部分 --%>
<%
    // 确保 session 中有用户信息，否则给他踢了
    UserInfo user = (UserInfo) session.getAttribute("user");
    if (user == null) {
        request.setAttribute("msg", "您尚未登录，请先登录");
        request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>聊天室</title>
</head>
<%-- 顶部：公告栏（含管理员操作按钮） --%>
<%-- 中部左侧：聊天消息列表 / 中部右侧：在线用户列表 --%>
<%-- 底部：消息发送栏 --%>
<frameset rows="80,*,80" frameborder="1" border="1">
    <frame src="${pageContext.request.contextPath}/jsp/notice.jsp" name="notice" scrolling="no" noresize>
    <frameset cols="75%,25%">
        <frame src="${pageContext.request.contextPath}/jsp/chatInfo.jsp" name="chatInfo">
        <frame src="${pageContext.request.contextPath}/jsp/online.jsp" name="online">
    </frameset>
    <frame src="${pageContext.request.contextPath}/jsp/send.jsp" name="send" scrolling="no" noresize>
</frameset>
</html>
