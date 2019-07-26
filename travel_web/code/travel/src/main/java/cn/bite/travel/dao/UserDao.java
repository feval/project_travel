package cn.bite.travel.dao;

import cn.bite.travel.domain.User;

/**
 * 用户相关的数据库访问接口层
 */
public interface UserDao {
    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    public User findByUsername(String username) ;


    /**
     * 保存用户
     * @param user
     */
    public void saveUser(User user) ;

}
