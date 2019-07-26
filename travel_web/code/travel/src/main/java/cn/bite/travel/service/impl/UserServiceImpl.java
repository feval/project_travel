package cn.bite.travel.service.impl;

import cn.bite.travel.dao.UserDao;
import cn.bite.travel.dao.impl.UserDaoImpl;
import cn.bite.travel.domain.User;
import cn.bite.travel.service.UserService;

/**
 * 用户相关的业务实现层
 */
public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl() ;
    /**
     * 用户注册功能
     * @param user
     * @return
     */
    @Override
    public boolean regist(User user) {
        //1.调用dao,通过用户名查询用户
        User u = userDao.findByUsername(user.getUsername());
        //判断当前用户是否为空
        if(u!=null){
            return false ;
        }
        //如果为空,直接返回true
        //保存用户
        userDao.saveUser(user);

        //注册成功了,激活邮件

        return true;
    }
}
