package com.lin.server;

import com.lin.util.CommUtils;
import com.lin.vo.MessageVO;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description:
 * Author:  llf
 * Created in 2019/8/23 16:33
 */
public class MultiThreadServer {
    private static final String IP;
    private static final int PORT;
    //缓存当前服务器所有在线的客户信息
    private static Map<String,Socket> clients=new ConcurrentHashMap<>();
    //缓存当前服务器所有的群名以及群好友
    private static Map<String,Set<String>> groups=new ConcurrentHashMap<>();
    static {
        Properties pros= CommUtils.loadProperties("socket.properties");
        IP=pros.getProperty("address");
        PORT=Integer.parseInt(pros.getProperty("port"));
    }

    private static class ExecuteClient implements Runnable{
        private Socket client;
        private Scanner in;
        private PrintStream out;
        public ExecuteClient(Socket client) {
            this.client=client;
            try {
                this.in=new Scanner(client.getInputStream());
                this.out=new PrintStream(client.getOutputStream(),true,"UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run() {
            while (true) {
                if (in.hasNextLine()) {
                    String jsonStrFromClient = in.nextLine();
                    MessageVO msgFromClient = (MessageVO) CommUtils.json2Object(jsonStrFromClient, MessageVO.class);
                    if (msgFromClient.getType().equals("1")) {
                        //获取客户发来的用户名
                        String userName=msgFromClient.getContent();
                        /**
                         * 将当前所有在线用户发送给新用户
                         * 将新用户上限信息发送给其他用户（上限提醒）
                         * 保存新用户上限信息
                         */
                        //将当前在线的所有用户发回客户端
                        MessageVO msg2Client=new MessageVO();
                        msg2Client.setType("1");
                        msg2Client.setContent(CommUtils.object2Json(clients.keySet()));
                        out.println(CommUtils.object2Json(msg2Client));
                        //将新上线的用户信息发回给当前以在线的所有用户
                        sendUserLogin("newLogin:"+userName);
                        //将当前用户注册到服务端缓存
                        clients.put(userName,client);
                        System.out.println(userName+"上线了！");
                        System.out.println("当前聊天室共有"+clients.size()+"人");
                    }else if (msgFromClient.getType().equals("2")) {
                        //用户私聊   Type2
                        //Content：myName-msg
                        //to：friendName
                        String friendName=msgFromClient.getTo();
                        Socket clientSocket=clients.get(friendName);
                        try{
                            PrintStream out=new PrintStream(clientSocket.getOutputStream(),true,"UTF-8");
                            MessageVO msg2Client=new MessageVO();
                            msg2Client.setType("2");
                            msg2Client.setContent(msgFromClient.getContent());
                            System.out.println("收到私聊消息，内容为："+msgFromClient.getContent());
                            out.println(CommUtils.object2Json(msg2Client));
                        }catch (IOException ex){
                            ex.printStackTrace();
                        }
                    }else if (msgFromClient.getType().equals("3")) {
                        String groupName=msgFromClient.getContent();
                        Set<String> friends= (Set<String>) CommUtils.json2Object(msgFromClient.getTo(), Set.class);
                        groups.put(groupName,friends);
                        System.out.println("有新的群注册成功，群名为："+groupName
                                +",一共有"+groups.size()+"个群");
                    }else if (msgFromClient.getType().equals("4")) {
                        // type:4
                        // content:myName-msg
                        // to:groupName
                        String groupName=msgFromClient.getTo();
                        Set<String> names=groups.get(groupName);
                        Iterator<String> iterator=names.iterator();
                        while (iterator.hasNext()) {
                            String socketName=iterator.next();
                            Socket client=clients.get(socketName);
                            try {
                                PrintStream out=new PrintStream(client.getOutputStream(),true,"UTF-8");
                                /**
                                 * typr=4
                                 * content直接转发
                                 * 组名-所有的群成员
                                 */
                                MessageVO messageVO=new MessageVO();
                                messageVO.setType("4");
                                messageVO.setContent(msgFromClient.getContent());
                                messageVO.setTo(groupName+"-"+CommUtils.object2Json(names));
                                out.println(CommUtils.object2Json(messageVO));
                                System.out.println("服务器发送的群聊消息为："+messageVO);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        private void sendUserLogin(String msg) {
            for(Map.Entry<String,Socket> entry:clients.entrySet()) {
                Socket socket=entry.getValue();
                try {
                    PrintStream out=new PrintStream(socket.getOutputStream(),true,"UTF-8");
                    out.println(msg);
                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket=new ServerSocket(PORT);
        ExecutorService executors = Executors.newFixedThreadPool(50);
        for (int i = 0; i < 50; i++) {
            System.out.println("等待客户端连接...");
            Socket client = serverSocket.accept();
            System.out.println("有新的客户端连接，端口号为"+client.getPort());
            executors.submit(new ExecuteClient(client));
        }
    }
}
