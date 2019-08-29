package com.lin.client.service;

import com.lin.util.CommUtils;
import com.lin.vo.MessageVO;
import jdk.nashorn.internal.scripts.JO;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.security.acl.Group;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:登陆成功后显示页面，设置私聊点击事件，创建群聊事件，群聊点击事件
 * Author:  llf
 * Created in 2019/8/23 22:17
 */
public class FriendsList {
    private JButton createGroupBtn;
    private JScrollPane friendsList;
    private JScrollPane groupListPanel;
    private JPanel friendsPanel;

    private JFrame frame;
    private String userName;
    private Set<String> users;
    private Connect2Server connect2Server;
    //存储所有的群名称和群好友
    private Map<String, Set<String>> groupMap = new ConcurrentHashMap<>();

    //缓存所有私聊界面
    private Map<String, PrivateChatGUI> privateChatGUIList = new ConcurrentHashMap<>();

    //缓存所有的群聊页面   群聊名  和群聊界面
    private Map<String, GroupChatGUI> groupChatGUIMap=new ConcurrentHashMap<>();

    //好友列表后台任务，不断监听服务器发来的信息，好友上限提醒，用户私聊，群聊
    private class DaemonTask implements Runnable {
        private Scanner in = new Scanner(connect2Server.getIn());

        @Override
        public void run() {
            while (true) {
                if (in.hasNextLine()) {
                    //接收服务到发来的信息
                    String strFromServer = in.nextLine();
                    //此时服务器端发来一个Json字串
                    if (strFromServer.startsWith("{")) {
                        MessageVO messageVO = (MessageVO) CommUtils.json2Object(strFromServer, MessageVO.class);
                        if (messageVO.getType().equals("2")) {
                            String friendName = messageVO.getContent().split("-")[0];
                            String msg = messageVO.getContent().split("-")[1];
                            //判断此私聊是不是第一次创建
                            if (privateChatGUIList.containsKey(friendName)) {
                                PrivateChatGUI privateChatGUI = privateChatGUIList.get(friendName);
                                privateChatGUI.getFreame().setVisible(true);
                                privateChatGUI.readFromServer(friendName + "说:" + msg);
                            } else {
                                PrivateChatGUI privateChatGUI = new PrivateChatGUI(friendName, userName, connect2Server);
                                privateChatGUIList.put(friendName, privateChatGUI);
                                privateChatGUI.readFromServer(friendName + "说:" + msg);
                            }
                        }else if (messageVO.getType().equals("4")) {
                            /**
                             * 收到服务器发来的群聊信息
                             * type
                             * content：sender-msg
                             * to:groupName-[]
                             */
                            String groupName = messageVO.getTo().split("-")[0];
                            String senderName = messageVO.getContent().split("-")[0];
                            String groupMsg = messageVO.getContent().split("-")[1];
                            //若此名称在群聊列表
                            if (groupMap.containsKey(groupName)) {
                                if (groupChatGUIMap.containsKey(groupName)) {
                                    //缓存中有群聊界面  弹出
                                    GroupChatGUI groupChatGUI = groupChatGUIMap.get(groupName);
                                    groupChatGUI.getFrame().setVisible(true);
                                    groupChatGUI.readFromServer(senderName+"说:"+groupMsg);
                                }else {
                                    //缓存中没有
                                    Set<String> names=groupMap.get(groupName);
                                    GroupChatGUI groupChatGUI=new GroupChatGUI(groupName,names,userName,connect2Server);
                                    groupChatGUIMap.put(groupName,groupChatGUI);
                                    groupChatGUI.readFromServer(senderName+"说:"+groupMsg);
                                }
                            }else {
                                /**
                                 * 用户第一次收到群聊消息
                                 */
                                Set<String> friends= (Set<String>) CommUtils.json2Object(messageVO.getTo().split("-")[1],
                                        Set.class);
                                groupMap.put(groupName,friends);
                                loadGroupList();
                                //弹出群聊界面
                                GroupChatGUI groupChatGUI = new GroupChatGUI(groupName,
                                        friends,userName,connect2Server);
                                groupChatGUIMap.put(groupName,groupChatGUI);
                                groupChatGUI.readFromServer(senderName+"说:"+groupMsg);
                            }
                        }
                    } else {
                        if (strFromServer.startsWith("newLogin:")) {               //**********************   好友上线逻辑
                            String newFriendName = strFromServer.split(":")[1];
                            users.add(newFriendName);
                            JOptionPane.showMessageDialog(frame,
                                    newFriendName + "上线了!", "上线提醒", JOptionPane.INFORMATION_MESSAGE);
                            //刷新好友列表
                            loadUsers();
                        }

                    }
                }
            }
        }

    }

    public FriendsList(String userName, Set<String> users, Connect2Server connect2Server) {
        this.userName = userName;
        this.users = users;
        this.connect2Server = connect2Server;
        frame = new JFrame(userName);
        frame.setContentPane(friendsPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        //使窗口显示在正中央
        frame.setLocationRelativeTo(null);
        //图形界面可视，     默认不可视
        frame.setVisible(true);
        //加载所有在线用户信息
        loadUsers();
        //启动后台线程不断监听服务器发来的消息*************************************
        Thread daemonThread = new Thread(new DaemonTask());
        //设置为守护线程
        daemonThread.setDaemon(true);
        daemonThread.start();
        //创建群组
        createGroupBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreateGroupGUI(userName, users, connect2Server, FriendsList.this);
            }
        });

    }

    private void loadUsers() {
        //创建好友列表的盘子
        JLabel[] userLables = new JLabel[users.size()];
        JPanel friends = new JPanel();
        //垂直布局
        friends.setLayout(new BoxLayout(friends, BoxLayout.Y_AXIS));

        Iterator<String> iterator = users.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            String userName = iterator.next();
            userLables[i] = new JLabel(userName);
            //添加标签点击事件********************************************************私聊起始
            userLables[i].addMouseListener(new PrivateLabelAction(userName));
            //将好友列表加到盘子中
            friends.add(userLables[i]);
            i++;
        }
        //将所有的好友的数组盘子加到friendsList的大盘子
        friendsList.setViewportView(friends);
        //设置滚动条垂直滚动
        friendsList.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //刷新
        friends.revalidate();
        friendsList.revalidate();
    }

    //群聊点击事件
    private class GroupLabelAction implements MouseListener {
        private String groupName;

        public GroupLabelAction(String groupName) {
            this.groupName=groupName;
        }
        @Override
        public void mouseClicked(MouseEvent e) {
            if (groupChatGUIMap.containsKey(groupName)) {
                GroupChatGUI groupChatGUI=groupChatGUIMap.get(groupName);
                groupChatGUI.getFrame().setVisible(true);
            }else {
                //  获得所有的群聊成员
                Set<String> names=groupMap.get(groupName);
                GroupChatGUI groupChatGUI=new GroupChatGUI(groupName,names,userName,connect2Server);
                groupChatGUIMap.put(groupName,groupChatGUI);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    //私聊点击事件
    private class PrivateLabelAction implements MouseListener {
        private String labelName;

        //根据标签名称跳转私聊界面
        public PrivateLabelAction(String labelName) {
            this.labelName = labelName;
        }

        //鼠标点击事件
        @Override
        public void mouseClicked(MouseEvent e) {
            //判断好友列表是否缓存私聊标签，
            if (privateChatGUIList.containsKey(labelName)) {
                //找到该标签使其显示
                PrivateChatGUI privateChatGUI = privateChatGUIList.get(labelName);
                privateChatGUI.getFreame().setVisible(true);
            } else {
                //第一次点击，创建私聊界面
                PrivateChatGUI privateChatGUI = new PrivateChatGUI(
                        labelName, userName, connect2Server);
                privateChatGUIList.put(labelName, privateChatGUI);
            }
        }

        //鼠标一直按下事件
        @Override
        public void mousePressed(MouseEvent e) {

        }

        //鼠标按下释放事件
        @Override
        public void mouseReleased(MouseEvent e) {

        }

        //鼠标移入执行事件
        @Override
        public void mouseEntered(MouseEvent e) {

        }

        //鼠标移出事件
        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    public void addGroup(String groupName, Set<String> friends) {
        groupMap.put(groupName, friends);
    }

    public void loadGroupList() {
        //存储所有群名称的标签Jpanel
        JPanel groupNamePanel = new JPanel();
        groupNamePanel.setLayout(new BoxLayout(groupNamePanel, BoxLayout.Y_AXIS));
        JLabel[] labels = new JLabel[groupMap.size()];
        //Map遍历
        Set<String> entries = groupMap.keySet();
        Iterator<String> iterator = entries.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            String groupName = iterator.next();
            labels[i] = new JLabel(groupName);
            //添加到自定义的盘子
            labels[i].addMouseListener(new GroupLabelAction(groupName));
            groupNamePanel.add(labels[i]);
            i++;
        }
        //将自定义盘子加到滚动条中
        groupListPanel.setViewportView(groupNamePanel);
        //设置垂直滚动
        groupListPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //刷新
        groupListPanel.revalidate();
    }
}










