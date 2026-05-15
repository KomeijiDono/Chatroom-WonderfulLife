/* ============================================================
 *  项目名称：Jsp_wonderfulLife
 *  脚本功能：数据库初始化（建库、建表、初始数据）
 *  目标数据库：Microsoft SQL Server
 * ============================================================ */

-- ============================
-- 1. 创建数据库
-- ============================
if not exists (select * from sys.databases where name = 'wonderfulLife')
begin
    create database wonderfulLife;
end
go

use wonderfulLife;
go

-- ============================
-- 2. 删除旧表（初始化时重建）
--    注意：先删有依赖关系的表，再删被依赖的表
-- ============================
if object_id('t_chat', 'U') is not null drop table t_chat;
if object_id('t_chatroom_config', 'U') is not null drop table t_chatroom_config;
if object_id('t_userInfo', 'U') is not null drop table t_userInfo;
go

-- ============================
-- 3. 创建用户信息表 t_userInfo
--    对应 POJO：UserInfo.java
--    对应 DAO ：UserInfoDao.java
--    涉及查询：
--      - 登录验证：SELECT * FROM t_userInfo WHERE account = ? AND password = ?
--      - 注册插入：INSERT INTO t_userInfo(account, password, type, online, sex, nickname) VALUES(?, ?, 0, 0, ?, ?)
--      - 查重    ：SELECT COUNT(*) FROM t_userInfo WHERE account = ?
--      - 在线更新：UPDATE t_userInfo SET online = ? WHERE account = ?
--      - 全离线  ：UPDATE t_userInfo SET online = 0 WHERE online = 1
--      - 在线列表：SELECT * FROM t_userInfo WHERE online = 1
--      - 管理员判：SELECT type FROM t_userInfo WHERE account = ?
-- ============================
create table t_userInfo (
    account  varchar(12)  not null primary key,  -- 用户账号（主键）
    password varchar(12)  not null,               -- 登录密码
    type     int          not null,               -- 用户类型：0=普通用户，1=管理员
    online   int          not null,               -- 在线状态：0=离线，1=在线
    sex      char(2)      not null check (sex in ('男', '女')),  -- 性别
    nickname varchar(20)  not null                -- 用户昵称
);
go

-- ============================
-- 4. 创建聊天消息表 t_chat
--    对应 POJO：Chat.java
--    对应 DAO ：ChatDao.java
--    涉及查询：
--      - 发消息  ：INSERT INTO t_chat(info, time, sender, color) VALUES(?, ?, ?, ?)
--      - 全部消息：SELECT c.*, u.nickname FROM t_chat c JOIN t_userInfo u ON c.sender = u.account ORDER BY c.id ASC
--      - 增量消息：SELECT c.*, u.nickname FROM t_chat c JOIN t_userInfo u ON c.sender = u.account WHERE c.id > ? ORDER BY c.id ASC
--      - 最大ID  ：SELECT ISNULL(MAX(id), 0) FROM t_chat
--      - 清空    ：DELETE FROM t_chat
-- ============================
create table t_chat (
    id     int          not null primary key identity(1,1),  -- 消息ID（自增主键）
    info   varchar(255) not null,       -- 消息内容（文本或HTML img 标签）
    time   varchar(20)  not null,       -- 发送时间（格式：yyyy-MM-dd HH:mm:ss）
    sender varchar(12)  not null,       -- 发送者账号（不做外键约束，避免用户删除后引用失效）
    color  varchar(20)  not null        -- 文字颜色（十六进制色值，如 #FF0000）
);
go

-- ============================
-- 5. 创建聊天室配置表 t_chatroom_config
--    对应 POJO：ChatroomConfig.java
--    对应 DAO ：ChatroomConfigDao.java
--    涉及查询：
--      - 查配置  ：SELECT * FROM t_chatroom_config WHERE id = 1
--      - 更新配置：UPDATE t_chatroom_config SET is_open = ?, manual_mode = ? WHERE id = 1
--    说明：本表为单行配置表，id 固定为 1
-- ============================
create table t_chatroom_config (
    id          int         not null primary key default 1,  -- 配置ID（固定为1）
    is_open     int         not null default 0,              -- 手动模式开关：0=关闭，1=开启
    manual_mode int         not null default 0,              -- 控制模式：0=自动模式，1=手动模式
    open_time   varchar(5)  not null default '09:00',        -- 自动开放时间（HH:mm）
    close_time  varchar(5)  not null default '18:00'         -- 自动关闭时间（HH:mm）
);
go

-- ============================
-- 6. 插入默认数据
-- ============================

-- 6a. 聊天室配置（默认：自动模式，开放时段 09:00-18:00）
if not exists (select * from t_chatroom_config where id = 1)
begin
    insert into t_chatroom_config (id, is_open, manual_mode, open_time, close_time)
    values (1, 0, 0, '09:00', '18:00');
end
go

-- 6b. 管理员账号（账号：admin / 密码：admin / 类型：管理员 / 性别：男 / 昵称：管理员）
if not exists (select * from t_userInfo where account = 'admin')
begin
    insert into t_userInfo (account, password, type, online, sex, nickname)
    values ('admin', 'admin', 1, 0, '男', '管理员');
end
go

-- ============================
-- 7. 验证安装（查询已创建的表结构）
-- ============================
-- 查看表信息
select 
    table_name  as '表名',
    table_type  as '表类型'
from information_schema.tables
where table_schema = 'dbo'
order by table_name;
go

-- 查看各表列信息
exec sp_help 't_userInfo';
go
exec sp_help 't_chat';
go
exec sp_help 't_chatroom_config';
go
