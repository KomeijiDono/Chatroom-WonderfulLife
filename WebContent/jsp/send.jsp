<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%-- 消息发送栏（嵌入聊天室底部 frame），支持文本消息和图片上传 --%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>发送消息</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/style.css">
<script src="${pageContext.request.contextPath}/js/jquery-4.0.0.min.js"></script>
<style>
.send-bar {
	display: flex;
	align-items: center;
	gap: 8px;
	padding: 10px 20px;
	background: #fff;
	border-top: 2px solid #667eea;
	height: 60px;
}

.send-bar input[type="text"] {
	flex: 1;
	padding: 8px 12px;
	border: 1px solid #ddd;
	border-radius: 4px;
	font-size: 14px;
	outline: none;
}

.send-bar input[type="text"]:focus {
	border-color: #667eea;
}

.send-bar select {
	padding: 8px;
	border: 1px solid #ddd;
	border-radius: 4px;
	font-size: 13px;
}

.send-bar .btn-send {
	padding: 8px 20px;
	background: linear-gradient(135deg, #667eea, #764ba2);
	color: #fff;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	font-size: 14px;
}

.send-bar .btn-send:hover {
	opacity: 0.9;
}

.send-bar .btn-img {
	padding: 8px 12px;
	background: #f0f0f0;
	border: 1px solid #ddd;
	border-radius: 4px;
	cursor: pointer;
	font-size: 13px;
}

.send-bar .btn-img:hover {
	background: #e0e0e0;
}

#imageInput {
	display: none;
}

.img-preview {
	display: none;
	max-width: 60px;
	max-height: 40px;
	border-radius: 4px;
}
</style>
</head>
<body>
	<div class="send-bar">
		<input type="text" id="msgInput" placeholder="输入消息..." maxlength="255">
		<select id="colorSelect">
			<option value="#000000">黑色</option>
			<option value="#ff0000">红色</option>
			<option value="#0000ff">蓝色</option>
			<option value="#008000">绿色</option>
			<option value="#ff6600">橙色</option>
			<option value="#800080">紫色</option>
			<!-- 继续添加自定义颜色，本来想做调色盘的 -->
		</select>
		<button class="btn-img"
			onclick="document.getElementById('imageInput').click()">图片</button>
		<input type="file" id="imageInput" accept="image/*"> <img
			id="imgPreview" class="img-preview">
		<button class="btn-send" onclick="sendMessage()">发送</button>
	</div>
	<script>
var contextPath = '${pageContext.request.contextPath}';
var pendingImageUrl = null; // 待发送的图片 URL（上传成功后暂存）

// 图片选择后自动上传到服务器
$('#imageInput').change(function() {
    var file = this.files[0];
    if (!file) return;

    var formData = new FormData();
    formData.append('image', file);

    $.ajax({
        url: contextPath + '/uploadImage',
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function(res) {
            if (res.success) {
                pendingImageUrl = res.url;
                $('#imgPreview').attr('src', pendingImageUrl).show();
                $('#msgInput').attr('placeholder', '已选择图片，可输入描述');
            } else {
                alert(res.msg);
            }
        }
    });
});

// 发送消息（文本或图片）
function sendMessage() {
    var info = $('#msgInput').val().trim();
    var color = $('#colorSelect').val();
    var type = 'text';

    // 如果有待发送的图片，优先发送图片
    if (pendingImageUrl) {
        info = pendingImageUrl;
        type = 'img';
    }

    if (!info && type === 'text') {
        alert('请输入消息内容');
        return;
    }

    $.post(contextPath + '/sendMessage', {info: info, color: color, type: type}, function(res) {
        if (res.success) {
            $('#msgInput').val('');
            if (pendingImageUrl) {
                pendingImageUrl = null;
                $('#imgPreview').hide().attr('src', '');
                $('#msgInput').attr('placeholder', '输入消息...');
            }
        } else if (res.redirect) {
            alert(res.msg);
            parent.location.href = res.redirect;
        }
    });
}

// 回车键也可以发送消息
$('#msgInput').keypress(function(e) {
    if (e.which == 13) sendMessage();
});
</script>
</body>
</html>
