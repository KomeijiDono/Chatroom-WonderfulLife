package com.edu.seiryo.wonderfulLife.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.edu.seiryo.wonderfulLife.pojo.ChatroomConfig;
import com.edu.seiryo.wonderfulLife.util.DBUtil;

/**
 * 聊天室配置数据访问对象
 * 提供聊天室开关、模式切换等配置的数据库操作
 */
public class ChatroomConfigDao {

    /** 获取聊天室配置（id=1 的单行配置表） */
    public ChatroomConfig getConfig() {
        String sql = "SELECT * FROM t_chatroom_config WHERE id = 1";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                ChatroomConfig config = new ChatroomConfig();
                config.setId(rs.getInt("id"));
                config.setIsOpen(rs.getInt("is_open"));
                config.setManualMode(rs.getInt("manual_mode"));
                config.setOpenTime(rs.getString("open_time"));
                config.setCloseTime(rs.getString("close_time"));
                return config;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return null;
    }

    /** 更新聊天室开关和模式设置 */
    public void updateConfig(ChatroomConfig config) {
        String sql = "UPDATE t_chatroom_config SET is_open = ?, manual_mode = ? WHERE id = 1";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, config.getIsOpen());
            ps.setInt(2, config.getManualMode());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, null);
        }
    }

    /** 判断聊天室当前是否开放（手动模式查isOpen，自动模式比较当前时间） */
    public boolean isChatroomOpen() {
        ChatroomConfig config = getConfig();
        if (config == null) {
            return false;
        }
        if (config.getManualMode() == 1) {
            return config.getIsOpen() == 1;
        }
        String now = new SimpleDateFormat("HH:mm").format(new Date());
        return now.compareTo(config.getOpenTime()) >= 0 && now.compareTo(config.getCloseTime()) < 0;
    }

    /** 判断当前时间是否在自动开放时段内 */
    public boolean isInAutoOpenPeriod() {
        ChatroomConfig config = getConfig();
        if (config == null) {
            return false;
        }
        String now = new SimpleDateFormat("HH:mm").format(new Date());
        return now.compareTo(config.getOpenTime()) >= 0 && now.compareTo(config.getCloseTime()) < 0;
    }
}
