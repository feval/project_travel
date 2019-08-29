package com.lin.client.dao;


import com.alibaba.druid.pool.DruidPooledConnection;
import com.lin.client.entity.User;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.*;


/**
 * Description:
 * Author:  llf
 * Created in 2019/8/23 16:30
 */
public class AccountDao extends BasedDao{
    public User userLogin(String userName, String password) {
        Connection connection=null;
        PreparedStatement statement=null;
        ResultSet resultSet=null;
        try{
            connection=getConnection();
            String sql="select * from user where username=? and password=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1,userName);
            //******解密   ********************
            statement.setString(2, DigestUtils.md5Hex(password));
            resultSet=statement.executeQuery();
            if (resultSet.next()) {
                User user=getUser(resultSet);
                return user;
            }
        }catch (SQLException e) {
            System.out.println("用户登陆失败");
            e.printStackTrace();
        }finally {
            closeResources(connection,statement,resultSet);
        }
        return null;
    }

    private User getUser(ResultSet resultSet) throws SQLException {
        User user=new User();
        user.setId(resultSet.getInt("id"));
        user.setUserName(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setBrief(resultSet.getString("brief"));
        return user;
    }

    public boolean userReg(User user) {
        Connection connection=null;
        PreparedStatement statement=null;
        try{
            connection = getConnection();
            String str="insert into user(username,password,brief) values (?,?,?)";
            //接收执行sql语句影响的行数，*************
            statement=connection.prepareStatement(str, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1,user.getUserName());

            //加密   *************
            statement.setString(2,DigestUtils.md5Hex(user.getPassword()));
            statement.setString(3,user.getBrief());
            int rows = statement.executeUpdate();
            if (rows==1) {
                return true;
            }
        }catch (SQLException ex) {
            System.out.println("用户注册失败");
            ex.printStackTrace();
        }finally {
            closeResources(connection,statement);
        }
        return false;
    }
}
