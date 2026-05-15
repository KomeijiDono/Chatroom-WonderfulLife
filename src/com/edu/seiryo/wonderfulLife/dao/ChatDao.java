package com.edu.seiryo.wonderfulLife.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.edu.seiryo.wonderfulLife.pojo.Chat;
import com.edu.seiryo.wonderfulLife.util.DBUtil;

/**
 * 聊天消息数据访问对象
 * 提供消息发送、查询、删除等数据库操作
 */
public class ChatDao {

    /** 数据版本号：每次清空聊天记录时递增，用于通知所有在线前端清空显示 */
    private static int dataVersion = 0;

    /** 获取当前数据版本号 */
    public static int getDataVersion() {
        return dataVersion;
    }

    /** 插入一条聊天消息 */
    public void sendMessage(Chat chat) {
        String sql = "INSERT INTO t_chat(info, time, sender, color) VALUES(?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, chat.getInfo());
            ps.setString(2, chat.getTime());
            ps.setString(3, chat.getSender());
            ps.setString(4, chat.getColor());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, null);
        }
    }

    /** 获取所有聊天消息（按id升序） */
    public List<Chat> getMessages() {
        String sql = "SELECT c.*, u.nickname FROM t_chat c JOIN t_userInfo u ON c.sender = u.account ORDER BY c.id ASC";
        List<Chat> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(buildChat(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return list;
    }

    /** 获取大于指定ID的新消息（用于轮询增量拉取） */
    public List<Chat> getMessagesAfter(int lastId) {
        String sql = "SELECT c.*, u.nickname FROM t_chat c JOIN t_userInfo u ON c.sender = u.account WHERE c.id > ? ORDER BY c.id ASC";
        List<Chat> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, lastId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(buildChat(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return list;
    }

    /** 获取当前最大消息ID（用于前端轮询的 lastId） */
    public int getMaxId() {
        String sql = "SELECT ISNULL(MAX(id), 0) FROM t_chat";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return 0;
    }

    /** 清空所有聊天记录（仅管理员可调用） */
    public void clearAll() {
        String sql = "DELETE FROM t_chat";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.executeUpdate();
            // 递增数据版本号，通知所有在线用户前端清空显示
            dataVersion++;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, null);
        }
    }

    /** 从 ResultSet 构建 Chat 对象 */
    private Chat buildChat(ResultSet rs) throws Exception {
        Chat chat = new Chat();
        chat.setId(rs.getInt("id"));
        chat.setInfo(rs.getString("info"));
        chat.setTime(rs.getString("time"));
        chat.setSender(rs.getString("sender"));
        chat.setColor(rs.getString("color"));
        chat.setNickname(rs.getString("nickname"));
        return chat;
    }
}
