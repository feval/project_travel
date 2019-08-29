package com.lin.client.service;

import com.lin.client.dao.AccountDao;
import com.lin.client.entity.User;
import com.lin.util.CommUtils;
import com.lin.vo.MessageVO;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.Set;

/**
 * Description:登陆成功，将当前用户所有的在线好友发送到服务器
 * Author:  llf
 * Created in 2019/8/23 19:04
 */
public class UserLogin {
    private JButton regButton;
    private JButton loginButton;
    private JTextField userNameText;
    private JPasswordField passwordText;
    private JPanel userPanel;
    private JPanel UserLoginPanel;
    private  JFrame frame;

    private AccountDao accountDao=new AccountDao();

    public UserLogin() {
        frame = new JFrame("用户登陆");
        frame.setContentPane(UserLoginPanel);
        //退出即关闭该对象
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //将窗口置于屏幕中央
        frame.setLocationRelativeTo(null);
        //根据页面布局设置窗口的大小
        frame.pack();
        //图形界面可视
        frame.setVisible(true);
        //注册按钮
        regButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UserReg();
            }
        });
        //登陆按钮
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //获取表单参数   校验用户信息
                String userName = userNameText.getText();
                String password = String.valueOf(passwordText.getPassword());
                User user = accountDao.userLogin(userName,password);
                if (user!=null) {
                    //显示成功信息**************
                    JOptionPane.showMessageDialog(frame,"登陆成功","提示信息",JOptionPane.INFORMATION_MESSAGE);
                    //登陆页面关闭
                    frame.setVisible(false);
                    /**
                     * 与服务器建立连接
                     * 读取服务端所有好友信息
                     * 新建一个后台进程不断读取服务器发来的信息
                     * type   1
                     * content  userName
                     * to   私聊  群聊
                     */
                    //以协议1的方式向服务器发送数据
                    Connect2Server connect2Server=new Connect2Server();
                    MessageVO msg2Server=new MessageVO();
                    msg2Server.setType("1");
                    msg2Server.setContent(userName);
                    String json2Server=CommUtils.object2Json(msg2Server);
                    try {
                        PrintStream out=new PrintStream(connect2Server.getOut(),true,"UTF-8");
                        //发送数据到服务器
                        out.println(json2Server);
                        //接收服务器发来的消息，显示所有在线好友，加载用户列表
                        /**
                         *                       type   1
                         *                       content 在线用户信息
                         *                       to
                         */
                        Scanner in=new Scanner(connect2Server.getIn());
                        if (in.hasNextLine()) {
                            String msgFromServerStr = in.nextLine();
                            MessageVO msgFromServer=(MessageVO) CommUtils.json2Object(msgFromServerStr,MessageVO.class);
                            //将服务器发送过来的信息封装成一个储存user对象的Set集合，传入好友列表进行显示，
                            Set<String> users = (Set<String>) CommUtils.json2Object(msgFromServer.getContent(), Set.class);
                            System.out.println("所有在线用户为：" + users);
                            //加载用户列表界面，将当前用户名，所有在线好友，与服务器建立连接，传递到好友列表界面***********
                            new FriendsList(userName,users,connect2Server);
                        }
                    } catch (UnsupportedEncodingException ex) {
                        ex.printStackTrace();
                    }

                }else {
                    //登陆失败，留在当前登陆页面，显示错误信息
                    JOptionPane.showMessageDialog(frame,"登陆失败！","错误信息",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) {
        UserLogin userLogin=new UserLogin();
    }
}
