package com.edu.seiryo.wonderfulLife.util;

import java.sql.*;

/**
 * 数据库工具类
 * 
 * 功能：
 * 1. 获取数据库连接
 * 2. 关闭资源
 * @author KomeijiDono
 *
 */
public class DBUtil {
	private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=wonderfulLife";

	private static final String USER = "sa";
	private static final String PASSWORD = "123456";

	// 静态代码块：类加载时自动注册 SQL Server JDBC 驱动
	static {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("加载驱动失败", e);
		}
	}

	/**
	 * 获取数据库连接
	 * 
	 * @return Connection对象
	 * @throws RuntimeException 当连接失败时抛出运行时异常
	 */
	public static Connection getConnection() {
		try {
			return DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e) {
			throw new RuntimeException("获取数据库连接失败", e);
		}
	}
	
	/**
	 * 关闭数据库资源（安全释放，不抛异常）
	 * 
	 * @param conn 数据库连接
	 * @param ps   预编译语句对象
	 * @param rs   结果集对象（允许为null）
	 */
	public static void close(Connection conn, PreparedStatement ps, ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
