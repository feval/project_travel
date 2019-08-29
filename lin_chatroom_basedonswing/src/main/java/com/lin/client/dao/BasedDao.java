package com.lin.client.dao;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.lin.client.entity.User;
import com.lin.util.CommUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Description:dao层基础类i，封装数据源，获取连接，关闭资源等操作
 * Author:  llf
 * Created in 2019/8/23 16:30
 */
public class BasedDao {
    //获取数据源
    private static DruidDataSource dataSource;
    static{
        Properties properties= CommUtils.loadProperties("db.properties");
        try {
            dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            System.out.println("获取数据源失败");
            e.printStackTrace();
        }
    }

    //获取连接
    protected DruidPooledConnection getConnection() {
        try {
            return (DruidPooledConnection) dataSource.getPooledConnection();
        } catch (SQLException e) {
            System.out.println("数据库连接失败");
            e.printStackTrace();
        }
        return null;
    }

    //关闭资源
    protected void closeResources(Connection connection,
                                  Statement statement) {
        try {
        if (connection != null) connection.close();
        if (statement != null) statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void closeResources(Connection connection,
                                  Statement statement,
                                  ResultSet resultSet) {
        closeResources(connection,statement);
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
