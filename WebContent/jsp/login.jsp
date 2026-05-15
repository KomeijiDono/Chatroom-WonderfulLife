<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%-- 用户登录页面 --%>
<%
    String msg = (String) request.getAttribute("msg"); // 从请求中获取提示消息
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>用户登录</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
	<div class="container">
		<div class="login-box">
			<h2>聊天室登录</h2>
			<% if (msg != null && !msg.isEmpty()) { %>
			<p class="msg"><%= msg %></p>
			<% } %>
			<form action="${pageContext.request.contextPath}/login" method="post">
				<div class="input-group">
					<label>账号：</label> <input type="text" name="account" required
						maxlength="12">
				</div>
				<div class="input-group">
					<label>密码：</label> <input type="password" name="password" required
						maxlength="12">
				</div>
				<button type="submit" class="btn">登录</button>
				<a href="${pageContext.request.contextPath}/jsp/register.jsp"
					class="link">没有账号？去注册</a>
			</form>
		</div>
	</div>
</body>
</html>
