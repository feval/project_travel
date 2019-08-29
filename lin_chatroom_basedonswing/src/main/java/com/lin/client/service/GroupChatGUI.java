package com.lin.client.service;

import com.lin.util.CommUtils;
import com.lin.vo.MessageVO;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Set;

/**
 * Description:
 * Author:  llf
 * Created in 2019/8/29 20:30
 */
public class GroupChatGUI {
    private JPanel groupPanel;
    private JTextArea readFromServer;
    private JTextField send2Server;
    private JPanel friendsPanel;

    private String groupName;
    private Set<String> friends;
    private String myName;
    private Connect2Server connect2Server;

    private JFrame frame;

    public GroupChatGUI(String groupName,
                        Set<String> friends,
                        String myName,
                        Connect2Server connect2Server) {
        frame= new JFrame("GroupChatGUI");
        frame.setContentPane(groupPanel);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(400,400);
        frame.setVisible(true);

        friendsPanel.setLayout(new BoxLayout(friendsPanel,
                BoxLayout.Y_AXIS));
        Iterator<String> iterator=friends.iterator();
        while (iterator.hasNext()) {
            String lableName=iterator.next();
            JLabel jLabel=new JLabel(lableName);
            friendsPanel.add(jLabel);
        }
        send2Server.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                StringBuilder sb=new StringBuilder();
                sb.append(send2Server.getText());
                //捕捉回车按键
                if (e.getKeyCode()==KeyEvent.VK_ENTER) {
                    String str2Server=sb.toString();
                    /**
                     * type:4
                     * content:myName-msg
                     * to:groupName
                     */
                    MessageVO messageVO=new MessageVO();
                    messageVO.setType("4");
                    messageVO.setContent(myName+"-"+str2Server);
                    messageVO.setTo(groupName);
                    try {
                        PrintStream out=new PrintStream(connect2Server.getOut(),true,"UTF-8");
                        out.println(CommUtils.object2Json(messageVO));
                        send2Server.setText("");
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    }

                }


            }
        });
    }

    public void readFromServer(String msg) {
        readFromServer.append(msg+"\n");
    }

    public JFrame getFrame() {
        return frame;
    }
}











