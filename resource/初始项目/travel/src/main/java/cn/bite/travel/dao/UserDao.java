package cn.bite.travel.dao;

import cn.bite.travel.domain.User;

/**
 * Description:
 * Author:  llf
 * Created in 2019/7/16 17:12
 */
public interface UserDao {
    public User findByUsername(String username);

    public void saveUser(User user);

    User findByCode(String code);

    void updateStatus(User user);

    User findByUsernaemAndPassword(String username, String password);
}
