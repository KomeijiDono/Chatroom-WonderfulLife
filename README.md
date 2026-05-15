# Wonderful Life 聊天室

一个基于 **JSP + Servlet + SQL Server** 的 Web 聊天室系统，支持用户注册登录、实时聊天（文本/图片）、在线用户列表、管理员控制等功能。

---

## 功能特性

### 用户端
- **用户注册** — 填写账号、密码、性别、昵称注册新账号
- **用户登录** — 账号密码验证，登录后自动标记在线
- **发送消息** — 支持文本消息，可自定义文字颜色
- **发送图片** — 上传图片自动转 HTML 显示
- **实时查看消息** — 每 2 秒轮询拉取新消息
- **在线用户列表** — 每 5 秒刷新在线用户
- **退出登录** — 清除在线状态并销毁会话

### 管理员端（type=1）
- **清除聊天内容** — 清空所有聊天记录，所有在线用户自动清屏
- **开启/关闭聊天室** — 手动控制聊天室开放状态
- **自动模式** — 按预设时间段（09:00-18:00）自动开关
- **不受开放时间限制** — 管理员可随时登录

### 安全机制
- **登录检查过滤器** — 未登录用户访问聊天页自动跳转登录页
- **聊天室开放检查过滤器** — 聊天室关闭时普通用户被强制下线
- **字符编码过滤器** — 统一 UTF-8 编码，防止中文乱码

---

## 技术栈

| 技术 | 说明 |
|------|------|
| Java | Servlet 3.1, JDK 8+ |
| JSP | JSP 2.3 + EL 表达式 |
| 前端 | jQuery 4.0.0, HTML5, CSS3 |
| 数据库 | SQL Server (通过 JDBC 连接) |
| 服务器 | Tomcat 8+ |
| JDBC 驱动 | sqljdbc4.jar |

---

## 项目结构

```
Jsp_wonderfulLife/
├── 数据库初始化.sql              # 建库建表脚本
├── 项目要求.md                   # 原始需求文档
├── src/
│   └── com/edu/seiryo/wonderfulLife/
│       ├── dao/                  # 数据访问层
│       │   ├── ChatDao.java           # 聊天消息 CRUD
│       │   ├── ChatroomConfigDao.java # 聊天室配置操作
│       │   └── UserInfoDao.java       # 用户信息操作
│       ├── pojo/                 # 实体类
│       │   ├── Chat.java              # 聊天消息实体
│       │   ├── ChatroomConfig.java    # 聊天室配置实体
│       │   └── UserInfo.java          # 用户信息实体
│       ├── servlet/              # 控制器层
│       │   ├── LoginServlet.java           # 用户登录
│       │   ├── RegisterServlet.java        # 用户注册
│       │   ├── LogoutServlet.java          # 退出登录
│       │   ├── SendMessageServlet.java     # 发送消息
│       │   ├── GetChatInfoServlet.java     # 获取聊天消息（轮询）
│       │   ├── GetOnlineUsersServlet.java  # 获取在线用户列表
│       │   ├── ClearChatServlet.java       # 管理员清除聊天
│       │   ├── ToggleChatroomServlet.java  # 管理员切换聊天室模式
│       │   ├── CheckChatroomStatusServlet.java # 检查聊天室状态
│       │   └── UploadImageServlet.java     # 图片上传
│       ├── filter/               # 过滤器
│       │   ├── CharsetFilter.java         # 字符编码过滤器
│       │   ├── CheckLoginFilter.java      # 登录检查过滤器
│       │   └── ChatroomOpenFilter.java    # 聊天室开放检查过滤器
│       └── util/                 # 工具类
│           ├── DBUtil.java               # 数据库连接工具
│           └── JsonUtil.java             # JSON 拼接工具
└── WebContent/
    ├── chat.jsp                  # 聊天室主页面（frameset 布局）
    ├── index.jsp                 # 网站入口（重定向到登录页）
    ├── css/
    │   └── style.css             # 全局样式表
    ├── js/
    │   └── jquery-4.0.0.min.js   # jQuery 库
    ├── images/                   # 存放上传的图片
    │   └── upload/               # 图片上传目录（自动创建）
    ├── jsp/                      # JSP 页面
    │   ├── login.jsp             # 登录页
    │   ├── register.jsp          # 注册页
    │   ├── logout.jsp            # 退出成功页
    │   ├── chatInfo.jsp          # 聊天消息面板（左中 frame）
    │   ├── online.jsp            # 在线用户面板（右中 frame）
    │   ├── notice.jsp            # 顶部公告栏（含管理员操作按钮）
    │   └── send.jsp              # 底部消息发送栏
    └── WEB-INF/
        ├── web.xml               # Web 部署描述文件
        └── lib/
            └── sqljdbc4.jar      # SQL Server JDBC 驱动
```

---

## 数据库设计

### 用户信息表（t_userInfo）

| 字段 | 类型 | 说明 |
|------|------|------|
| account | varchar(12) | 账号（主键，5-12位） |
| password | varchar(12) | 密码（5-12位） |
| type | int | 用户类型：0=普通用户, 1=管理员 |
| online | int | 在线状态：0=离线, 1=在线 |
| sex | char(2) | 性别（男/女） |
| nickname | varchar(20) | 昵称（最长10个汉字） |

### 聊天消息表（t_chat）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | int | 消息ID（自增主键） |
| info | varchar(255) | 消息内容（文本或HTML img标签） |
| time | varchar(20) | 发送时间（yyyy-MM-dd HH:mm:ss） |
| sender | varchar(12) | 发送者账号（不使用外键） |
| color | varchar(20) | 文字颜色（十六进制色值） |

### 聊天室配置表（t_chatroom_config）

| 字段 | 类型 | 说明 |
|------|------|------|
| id | int | 配置ID（固定为1） |
| is_open | int | 手动模式开关：0=关闭, 1=开启 |
| manual_mode | int | 控制模式：0=自动, 1=手动 |
| open_time | varchar(5) | 自动开放时间（HH:mm） |
| close_time | varchar(5) | 自动关闭时间（HH:mm） |

### 默认数据

- **管理员账号**：`admin` / `admin`（type=1，不受聊天室开放时间限制）
- **普通账号**：`luojiesi` / `123456`（type=0）
- **聊天室配置**：自动模式，开放时间 09:00-18:00

---

## 环境搭建与运行

### 前置条件

| 环境 | 版本要求 |
|------|---------|
| JDK | 8 或更高版本 |
| Tomcat | 8 或更高版本 |
| SQL Server | 2008 或更高版本 |
| Eclipse | 任意支持 Java EE 的版本 |

### 安装步骤

#### 1. 数据库初始化

打开 SQL Server Management Studio，执行 `数据库初始化.sql` 脚本：

```sql
-- 该脚本会自动：
-- 1. 创建 wonderfulLife 数据库（如不存在）
-- 2. 创建 t_userInfo、t_chat、t_chatroom_config 三张表
-- 3. 插入默认管理员账号 (admin / admin123)
-- 4. 插入默认聊天室配置 (自动模式 09:00-18:00)
```

#### 2. 配置数据库连接

修改 `src/com/edu/seiryo/wonderfulLife/util/DBUtil.java` 中的连接参数：

```java
private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=wonderfulLife";
private static final String USER = "sa";          // 改为你的数据库用户名
private static final String PASSWORD = "123456";  // 改为你的数据库密码
```

> **注意**：确保 SQL Server 已启用 TCP/IP 协议（SQL Server Configuration Manager → SQL Server 网络配置 → MSSQLSERVER 协议 → TCP/IP 启用）。

#### 3. 导入 Eclipse

1. `File` → `Import` → `General` → `Existing Projects into Workspace`
2. 选择项目根目录 `Jsp_wonderfulLife`
3. 确保 `WebContent/WEB-INF/lib/sqljdbc4.jar` 在 Build Path 中

#### 4. 部署到 Tomcat

1. 右键项目 → `Run As` → `Run on Server`
2. 选择已配置的 Tomcat 服务器
3. 访问 `http://localhost:8080/Jsp_wonderfulLife`

#### 5. 首次登录

使用默认管理员账号登录：
- 账号：`admin`
- 密码：`admin`

---

## API 接口一览

| 路径 | 方法 | 说明 | 权限 |
|------|------|------|------|
| `/login` | POST | 用户登录 | 无 |
| `/register` | POST | 用户注册 | 无 |
| `/logout` | GET | 退出登录 | 已登录 |
| `/sendMessage` | POST | 发送消息 | 已登录 + 聊天室开放 |
| `/getChatInfo` | GET | 获取聊天消息（支持增量lastId） | 已登录 + 聊天室开放 |
| `/getOnlineUsers` | GET | 获取在线用户列表 | 已登录 + 聊天室开放 |
| `/uploadImage` | POST | 上传图片（multipart） | 已登录 + 聊天室开放 |
| `/clearChat` | GET | 清除所有聊天记录 | 管理员 |
| `/toggleChatroom` | GET | 切换聊天室模式(open/close/auto) | 管理员 |
| `/checkChatroomStatus` | GET | 查询聊天室状态 | 已登录 |

---

## 常见问题与解决方案

### Q: 发送消息后数据库没有数据
已通过删除t_chat表sender值的外键约束解决。

### Q: 管理员清除聊天后其他用户页面未清空
已通过**数据版本号机制**解决。每次清除聊天时版本号递增，所有用户前端自动检测并清屏。

### Q: 聊天室关闭后用户未强制下线
已通过**过滤器拦截 + 前端检测**解决。关闭聊天室后，所有用户的轮询请求被拦截，前端检测到后自动跳转登录页。

### Q: 中文乱码
`CharsetFilter` 已将所有请求和响应设置为 UTF-8 编码。

---

## 开发说明

- **JSP 页面**使用 frameset 布局，分为顶部公告栏、中部（左：聊天消息 / 右：在线用户）、底部发送栏
- **前端轮询**使用 jQuery 的 `$.get()` 定时拉取数据，非 WebSocket 实时推送
- **聊天室开放控制**支持自动模式（按时间段）和手动模式（管理员开关）
- **管理员操作**不受聊天室开放状态限制，`ChatroomOpenFilter` 中对管理员直接放行

---

## 开源协议

本项目采用 [MIT License](LICENSE) 进行许可。

---

## 作者

- **KomeijiDono**
- 项目地址：https://github.com/KomeijiDono/Chatroom-WonderfulLife

如有问题或建议，欢迎提交 Issue 或 Pull Request。
