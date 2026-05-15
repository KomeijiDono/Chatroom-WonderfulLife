<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- 网站入口：直接重定向到登录页面 --%>
<%
    response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
%>
