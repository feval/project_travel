package com.lin.client.service;

import com.lin.client.dao.AccountDao;
import com.lin.client.entity.User;
import jdk.nashorn.internal.scripts.JO;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Description:
 * Author:  llf
 * Created in 2019/8/23 19:20
 */
public class UserReg {
    private JTextField userNameText;
    private JPasswordField passwordText;
    private JTextField brifeText;
    private JButton regBtn;
    private JPanel userRegPanel;

    private AccountDao accountDao=new AccountDao();

    public UserReg() {
        JFrame frame = new JFrame("用户注册");
        //包含这个盘子下的所有盘子
        frame.setContentPane(userRegPanel);
        //设置关闭即该对象永久消失
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //使窗口显示在正中央
        frame.setLocationRelativeTo(null);
        //根据布局来确定窗口的最佳大小
        frame.pack();
        //图形界面可视，     默认不可视
        frame.setVisible(true);

        //点击注册按钮，将信息持久化到db中，成功弹出提示框
        regBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取表单参数
                String userName = userNameText.getText();
                //获取经过密码********
                String password = String.valueOf(passwordText.getPassword());
                String brief=brifeText.getText();
                //封装User保存到数据库中
                User user=new User();
                user.setUserName(userName);
                user.setPassword(password);
                user.setBrief(brief);
                if (accountDao.userReg(user)) {
                    //注册成功
                    JOptionPane.showMessageDialog(frame,"注册成功!","提示信息",JOptionPane.INFORMATION_MESSAGE);
                    frame.setVisible(false);
                }else {
                    //注册失败
                    JOptionPane.showMessageDialog(frame,"注册失败!","错误信息",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
