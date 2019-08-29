package com.lin.client.service;

import com.lin.util.CommUtils;
import com.lin.vo.MessageVO;
import javafx.scene.effect.SepiaTone;
import sun.plugin2.message.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Description:
 * Author:  llf
 * Created in 2019/8/29 18:43
 */
public class CreateGroupGUI {
    private JPanel createGroupPanel;
    private JTextField groupNameText;
    private JButton conformBtn;
    private JPanel friendLabelPanel;

    private String myName;
    private Set<String> friends;
    private Connect2Server connect2Server;
    private FriendsList friendsList;

    public CreateGroupGUI(String myName,
                          Set<String> friends,
                          Connect2Server connect2Server,
                          FriendsList friendsList){
        this.myName=myName;
        this.friends=friends;
        this.connect2Server=connect2Server;
        this.friendsList=friendsList;
        JFrame frame = new JFrame("创建群聊");
        frame.setContentPane(createGroupPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,300);
        //居中
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        //将在线好友以checkBox展示到界面中
        friendLabelPanel.setLayout(new BoxLayout(friendLabelPanel,BoxLayout.Y_AXIS));
        Iterator<String> iterator=friends.iterator();
        while (iterator.hasNext()) {
            String lableName=iterator.next();
            JCheckBox checkBox=new JCheckBox(lableName);
            friendLabelPanel.add(checkBox);
        }
        friendLabelPanel.revalidate();

        conformBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Set<String> selectedFriends=new HashSet<>();
                Component[] comps=friendLabelPanel.getComponents();
                for (Component comp : comps) {
                    //向下转型
                    JCheckBox checkBox= (JCheckBox) comp;
                    if (checkBox.isSelected()) {
                        String labelName= checkBox.getText();
                        selectedFriends.add(labelName);
                    }
                }
                //创建记得加入自己的名字
                selectedFriends.add(myName);
                //获取群名
                String groupName=groupNameText.getText();
                //发送给服务器
                MessageVO messageVO=new MessageVO();
                /**
                 * 第三次协议
                 */
                messageVO.setType("3");
                messageVO.setContent(groupName);
                messageVO.setTo(CommUtils.object2Json(selectedFriends));
                try {
                    PrintStream out=new PrintStream(connect2Server.getOut(),true,"UTF-8");
                    out.println(CommUtils.object2Json(messageVO));
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }
                //将当前群聊界面隐藏
                frame.setVisible(false);
                friendsList.addGroup(groupName,selectedFriends);
                friendsList.loadGroupList();
            }
        });
    }

}
