package cn.bite.travel.service.impl;

import cn.bite.travel.dao.UserDao;
import cn.bite.travel.dao.impl.UserDaoImpl;
import cn.bite.travel.domain.User;
import cn.bite.travel.service.UserService;
import cn.bite.travel.util.MailUtils;
import cn.bite.travel.util.UuidUtil;

/**
 * Description:
 * Author:  llf
 * Created in 2019/7/16 17:10
 */
public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();

    @Override
    public boolean regist(User user) {
        User u=userDao.findByUsername(user.getName());
        if (u!=null) {
            return false;
        }
        //给用户设置激活码
        user.setCode(UuidUtil.getUuid()); //生成随机码
        //的何止当前的激活状态(N,没有激活)
        user.setStatus("N");
        userDao.saveUser(user);


        //注册成功   激活邮件
        //发送邮件
        //收件人邮箱       邮件内容     邮件标题
        //
        //    public static boolean sendMail(String to, String text, String title)
        String content="您的邮箱需要激活,请<a href='http://localhost/travel/user/active?code="+user.getCode()+"'>点击激活</a>";
        MailUtils.sendMail(user.getEmail(),content,"激活邮件");
        return true;
    }

    /**
     * yonghu jihuo
     * @param code
     * @return
     */
    @Override
    public boolean active(String code) {
        User user =userDao.findByCode(code);
        if (user!=null) {
            userDao.updateStatus(user);
            return true;
        }else {
            return false;
        }
    }

    /**
     * 根据用户名和密码查询用户
     * @param user
     * @return
     */
    @Override
    public User findByUsernaemAndPassword(User user) {
        return userDao.findByUsernaemAndPassword(user.getUsername(),user.getPassword());
    }
}
