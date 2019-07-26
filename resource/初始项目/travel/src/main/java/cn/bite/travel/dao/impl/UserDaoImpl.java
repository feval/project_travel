package cn.bite.travel.dao.impl;

import cn.bite.travel.dao.UserDao;
import cn.bite.travel.domain.User;
import cn.bite.travel.util.JDBCUtils;
import com.sun.mail.imap.protocol.UIDSet;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.MetaDataAccessException;

/**
 * Description:
 * Author:  llf
 * Created in 2019/7/16 17:12
 */
public class UserDaoImpl implements UserDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());


    @Override
    public User findByUsername(String username) {
        User user = null;
        try {
            String sql = "select * from tab_user where username=?";
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username);
        } catch (Exception e) {

        }
        return user;

    }
    @Override
    public void saveUser(User user) {
        //准备sql
        String sql = "insert into tab_user(username,password,name,birthday,sex,telephone,email,status,code) values(?,?,?,?,?,?,?,?,?)";
        //执行更新
        template.update(sql, user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getBirthday(),
                user.getSex(),
                user.getTelephone(),
                user.getEmail(),
                user.getStatus(),
                user.getCode());
    }

    @Override
    public User findByCode(String code) {
        User user = null;
        try {
            String sql = "select * from tab_user where code=?";
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), code);
        }catch (DataAccessException e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void updateStatus(User user) {
        String sql="update tab_user set status 'Y'where uid=?";
        template.update(sql,user.getUid());
    }

    /**
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public User findByUsernaemAndPassword(String username, String password) {
        User user = null;
        try {
            String sql = "select * from tab_user where username=? and password=?";
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username,password);
        } catch (Exception e) {

        }
        return user;
    }
}
