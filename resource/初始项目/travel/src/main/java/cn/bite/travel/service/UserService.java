package cn.bite.travel.service;

import cn.bite.travel.domain.User;

/**
 * Description:
 * Author:  llf
 * Created in 2019/7/16 17:10
 */
public interface UserService {
    boolean regist(User user);

    boolean active(String code);

    User findByUsernaemAndPassword(User user);
}
