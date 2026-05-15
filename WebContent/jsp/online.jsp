<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%-- 在线用户列表面板（嵌入聊天室右侧 frame） --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>在线用户</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
<script src="${pageContext.request.contextPath}/js/jquery-4.0.0.min.js"></script>
<style>
.online-container {
	padding: 10px;
	height: 100%;
	overflow-y: auto;
	background: #fff;
}

.online-container h3 {
	margin: 0 0 10px 0;
	font-size: 16px;
	color: #667eea;
	text-align: center;
}

.online-user {
	padding: 6px 10px;
	margin: 4px 0;
	background: #f0f2ff;
	border-radius: 6px;
	font-size: 13px;
}

.online-user .admin-tag {
	color: #ff4757;
	font-weight: bold;
	font-size: 11px;
	margin-left: 5px;
}

.count {
	text-align: center;
	font-size: 12px;
	color: #999;
	margin-bottom: 10px;
}
</style>
</head>
<body>
	<div class="online-container">
		<h3>在线用户</h3>
		<div class="count" id="userCount">当前 0 人在线</div>
		<div id="userList"></div>
	</div>
	<script>
var contextPath = '${pageContext.request.contextPath}';

// 从服务端拉取在线用户列表并渲染
function loadOnlineUsers() {
    $.get(contextPath + '/getOnlineUsers', function(data) {
        // 检测聊天室是否已被管理员关闭（过滤器拦截返回的信息）
        if (data && data.success === false && data.redirect) {
            alert(data.msg || '聊天室已关闭');
            parent.location.href = data.redirect;
            return;
        }
        var html = '';
        $('#userList').empty();
        for (var i = 0; i < data.length; i++) {
            var u = data[i];
            var tag = u.type == 1 ? '<span class="admin-tag">[管理员]</span>' : '';
            html += '<div class="online-user">' + u.nickname + tag + '</div>';
        }
        $('#userList').html(html);
        $('#userCount').text('当前 ' + data.length + ' 人在线');
    });
}

// 页面加载后立即拉取一次，之后每 5 秒轮询
$(function() {
    loadOnlineUsers();
    setInterval(loadOnlineUsers, 5000);
});
</script>
</body>
</html>
