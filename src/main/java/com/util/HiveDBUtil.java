package com.util;

import java.sql.*;

/**
 * @Author wuxin
 * @Date 2023/6/30 16:53
 * @Description
 * @Version
 */
public class HiveDBUtil {
    private HiveDBUtil() {}

    private static String url = "jdbc:hive2://10.90.6.168:10000/oa";
    private static String userName = "root";
    private static String password = "123456";

    static {
        try {
            Class.forName("org.apache.hive.jdbc.HiveDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 用来执行命令
     * @param sql  相关命令
     * @return
     */
    public static int update(String sql) {
        Connection conn=null;
        Statement stm=null;

        try {
            conn=getConn();
            stm=conn.createStatement();
            return stm.executeUpdate(sql);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }finally {
            close(null,stm,conn);
        }
    }

    /**
     * 得到hive连接
     * @return 连接
     */
    public static Connection getConn() {
        try {
            return DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 清理资源
     * @param conn
     * @param stm
     * @param rs
     */
    public static void close(ResultSet rs, Statement stm, Connection conn ) {

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (stm != null) {
            try {
                stm.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}


