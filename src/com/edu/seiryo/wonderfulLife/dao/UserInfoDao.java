package com.edu.seiryo.wonderfulLife.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.edu.seiryo.wonderfulLife.pojo.UserInfo;
import com.edu.seiryo.wonderfulLife.util.DBUtil;

/**
 * 用户信息数据访问对象
 * 提供用户登录、注册、在线状态管理等数据库操作
 */
public class UserInfoDao {

    /** 根据账号密码查询用户（用于登录验证） */
    public UserInfo login(String account, String password) {
        String sql = "SELECT * FROM t_userInfo WHERE account = ? AND password = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, account);
            ps.setString(2, password);
            rs = ps.executeQuery();
            if (rs.next()) {
                UserInfo user = new UserInfo();
                user.setAccount(rs.getString("account"));
                user.setPassword(rs.getString("password"));
                user.setType(rs.getInt("type"));
                user.setOnline(rs.getInt("online"));
                user.setSex(rs.getString("sex"));
                user.setNickname(rs.getString("nickname"));
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return null;
    }

    /** 注册新用户（默认 type=0 普通用户, online=0） */
    public boolean register(UserInfo user) {
        String sql = "INSERT INTO t_userInfo(account, password, type, online, sex, nickname) VALUES(?, ?, 0, 0, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getAccount());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getSex());
            ps.setString(4, user.getNickname());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            DBUtil.close(conn, ps, null);
        }
    }

    /** 检查账号是否已被注册 */
    public boolean isAccountExist(String account) {
        String sql = "SELECT COUNT(*) FROM t_userInfo WHERE account = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, account);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return false;
    }

    /** 设置用户在线/离线状态 */
    public void setOnline(String account, int status) {
        String sql = "UPDATE t_userInfo SET online = ? WHERE account = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, status);
            ps.setString(2, account);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, null);
        }
    }

    /** 将所有在线用户强制设置为离线（聊天室关闭时调用） */
    public void setOfflineAll() {
        String sql = "UPDATE t_userInfo SET online = 0 WHERE online = 1";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, null);
        }
    }

    /** 获取所有在线用户列表（用于在线用户面板展示） */
    public List<UserInfo> getOnlineUsers() {
        String sql = "SELECT * FROM t_userInfo WHERE online = 1";
        List<UserInfo> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                UserInfo user = new UserInfo();
                user.setAccount(rs.getString("account"));
                user.setNickname(rs.getString("nickname"));
                user.setSex(rs.getString("sex"));
                user.setType(rs.getInt("type"));
                list.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return list;
    }

    /** 判断指定账号是否为管理员 */
    public boolean isAdmin(String account) {
        String sql = "SELECT type FROM t_userInfo WHERE account = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, account);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("type") == 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, ps, rs);
        }
        return false;
    }
}
