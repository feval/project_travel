package com.lin.client.service;

import com.lin.util.CommUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Properties;

/**
 * Description:封装客户端与服务端建立的连接以及输入输出流
 * Author:  llf
 * Created in 2019/8/23 19:45
 */
public class Connect2Server {
    private static final String IP;
    private static final int PORT;
    static {
        Properties properties = CommUtils.loadProperties("socket.properties");
        IP=properties.getProperty("address");
        PORT= Integer.parseInt(properties.getProperty("port"));
    }
    private Socket client;
    private InputStream in;
    private OutputStream out;

    public Connect2Server() {
        try {
            client=new Socket(IP,PORT);
            in=client.getInputStream();
            out=client.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InputStream getIn() {
        return in;
    }

    public OutputStream getOut() {
        return out;
    }
}
