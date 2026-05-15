<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.edu.seiryo.wonderfulLife.pojo.UserInfo"%>
<%-- 顶部公告栏（显示欢迎信息和管理员操作按钮） --%>
<%
    UserInfo user = (UserInfo) session.getAttribute("user");
    boolean isAdmin = user != null && user.getType() == 1; // 判断是否为管理员
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>公告</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
<script src="${pageContext.request.contextPath}/js/jquery-4.0.0.min.js"></script>
<style>
.notice-bar {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 5px 20px;
	height: 70px;
	background: linear-gradient(135deg, #667eea, #764ba2);
	color: #fff;
}

.notice-bar h2 {
	margin: 0;
	font-size: 18px;
}

.notice-bar .user-info {
	font-size: 14px;
}

.notice-bar .user-info span {
	margin-right: 15px;
}

.notice-bar .admin-actions {
	display: flex;
	gap: 8px;
}

.notice-bar .admin-actions button {
	padding: 5px 12px;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	font-size: 12px;
}

.btn-clear {
	background: #ff4757;
	color: #fff;
}

.btn-toggle {
	background: #2ed573;
	color: #fff;
}

.btn-logout {
	background: #fff;
	color: #333;
	text-decoration: none;
	padding: 5px 12px;
	border-radius: 4px;
	font-size: 12px;
}
</style>
</head>
<body>
	<div class="notice-bar">
		<h2>Wonderful Life 聊天室</h2>
		<div class="user-info">
			<span>欢迎您，<%= user.getNickname() %></span> <span><%= isAdmin ? "[管理员]" : "[普通用户]" %></span>
		</div>
		<div class="admin-actions">
			<% if (isAdmin) { %>
			<button class="btn-clear" onclick="clearChat()">清除聊天内容</button>
			<button class="btn-toggle" onclick="toggleChatroom('open')">开启聊天室</button>
			<button class="btn-toggle" onclick="toggleChatroom('close')">关闭聊天室</button>
			<button class="btn-toggle" onclick="toggleChatroom('auto')">自动模式</button>
			<% } %>
			<a href="javascript:doLogout()" class="btn-logout">退出</a>
		</div>
	</div>
	<script>
// 退出登录（让顶层窗口跳转，避免只在当前 frame 内跳转）
function doLogout() {
    top.location.href = '${pageContext.request.contextPath}/logout';
}
// 清除所有聊天记录（需管理员权限）
function clearChat() {
    if (!confirm('确定要清除所有聊天内容吗？')) return;
    $.get('${pageContext.request.contextPath}/clearChat', function(res) {
        if (res.success) {
            alert('聊天内容已清除');
            // 重置聊天消息面板：清空显示、重置 lastId、重新拉取
            try {
                var chatInfoWin = parent.document.querySelector('frame[name="chatInfo"]').contentWindow;
                chatInfoWin.resetChatDisplay();
            } catch(e) {
                // 实在还有问题就重新加载整个聊天室
                parent.location.reload();
            }
        } else {
            alert(res.msg);
        }
    });
}
// 切换聊天室模式（开启/关闭/自动）
function toggleChatroom(action) {
    $.get('${pageContext.request.contextPath}/toggleChatroom', {action: action}, function(res) {
        if (res.success) {
            var msg = action === 'open' ? '聊天室已手动开启' :
                      action === 'close' ? '聊天室已手动关闭' : '已切换为自动模式';
            alert(msg);
        } else {
            alert(res.msg);
        }
    });
}
</script>
</body>
</html>
