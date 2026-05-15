<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%-- 用户注册页面 --%>
<%
    String msg = (String) request.getAttribute("msg"); // 从请求中获取提示消息
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>用户注册</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
	<div class="container">
		<div class="login-box">
			<h2>用户注册</h2>
			<% if (msg != null && !msg.isEmpty()) { %>
			<p class="msg"><%= msg %></p>
			<% } %>
			<form action="${pageContext.request.contextPath}/register"
				method="post">
				<div class="input-group">
					<label>账号（5-12位）：</label> <input type="text" name="account"
						required minlength="5" maxlength="12">
				</div>
				<div class="input-group">
					<label>密码（5-12位）：</label> <input type="password" name="password"
						required minlength="5" maxlength="12">
				</div>
				<div class="input-group">
					<label>性别：</label> <label class="radio-label"><input
						type="radio" name="sex" value="男" checked> 男 <input
						type="radio" name="sex" value="女"> 女</label>
				</div>
				<div class="input-group">
					<label>昵称：</label> <input type="text" name="nickname" required
						maxlength="20">
				</div>
				<button type="submit" class="btn">注册</button>
				<a href="${pageContext.request.contextPath}/jsp/login.jsp"
					class="link">已有账号？去登录</a>
			</form>
		</div>
	</div>
</body>
</html>
