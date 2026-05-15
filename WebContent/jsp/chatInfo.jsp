<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%-- 聊天消息列表面板（嵌入聊天室中部左侧 frame），通过 AJAX 轮询获取消息 --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>聊天内容</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
<script src="${pageContext.request.contextPath}/js/jquery-4.0.0.min.js"></script>
<style>
.chat-container {
	padding: 10px;
	height: 100%;
	overflow-y: auto;
	background: #f8f9fa;
}

.chat-msg {
	margin: 8px 0;
	padding: 8px 12px;
	background: #fff;
	border-radius: 8px;
	box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.chat-msg .meta {
	font-size: 12px;
	color: #888;
	margin-bottom: 4px;
}

.chat-msg .meta .sender {
	font-weight: bold;
	color: #333;
}

.chat-msg .content {
	font-size: 14px;
	word-break: break-all;
}

.system-msg {
	text-align: center;
	color: #999;
	font-size: 12px;
	padding: 5px;
}
</style>
</head>
<body>
	<div class="chat-container" id="chatContainer"></div>
	<script>
var lastId = 0;                    // 已加载的最大消息ID（用于增量拉取）
var localDataVersion = 0;          // 本地数据版本号（用于检测聊天记录是否被清空）
var contextPath = '${pageContext.request.contextPath}';

// 从服务端拉取新消息（增量轮询）
function loadMessages() {
    $.get(contextPath + '/getChatInfo', {lastId: lastId}, function(data) {
        // 检测聊天室是否已被管理员关闭（过滤器拦截返回的信息）
        if (data.success === false && data.redirect) {
            alert(data.msg || '聊天室已关闭');
            parent.location.href = data.redirect;
            return;
        }
        // 检测数据版本号是否变化（管理员清除聊天内容后所有用户实时清屏）
        if (data.dataVersion !== undefined && data.dataVersion !== localDataVersion) {
            $('#chatContainer').empty();
            lastId = 0;
            localDataVersion = data.dataVersion;
            // 重置后重新拉取消息（此时数据库已清空，拉取结果为空）
            loadMessages();
            return;
        }
        if (data.messages && data.messages.length > 0) {
            for (var i = 0; i < data.messages.length; i++) {
                var msg = data.messages[i];
                displayMessage(msg);
            }
            lastId = data.maxId;
            // 自动滚动到底部显示最新消息
            var container = document.getElementById('chatContainer');
            container.scrollTop = container.scrollHeight;
        }
    });
}

// 在页面上渲染一条聊天消息
function displayMessage(msg) {
    var html = '<div class="chat-msg">';
    html += '<div class="meta"><span class="sender">' + (msg.nickname || msg.sender) + '</span> <span>' + msg.time + '</span></div>';
    html += '<div class="content" style="color:' + msg.color + '">' + msg.info + '</div>';
    html += '</div>';
    $('#chatContainer').append(html);
}

// 定期检查聊天室是否仍开放（每 30 秒）
function checkRoomStatus() {
    $.get(contextPath + '/checkChatroomStatus', function(data) {
        if (!data.isOpen) {
            alert('聊天室已关闭，即将返回登录页面');
            parent.location.href = contextPath + '/jsp/login.jsp';
        }
    });
}

// 重置聊天消息面板（供管理员清除聊天后调用）
function resetChatDisplay() {
    $('#chatContainer').empty();
    lastId = 0;
    loadMessages();
}

$(function() {
    loadMessages();                // 初始加载
    setInterval(loadMessages, 2000);     // 每 2 秒轮询新消息
    setInterval(checkRoomStatus, 3000);  // 每 3 秒检查聊天室状态
});
</script>
</body>
</html>
