package com.lin.vo;

/**
 * Description:
 * Author:  llf
 * Created in 2019/8/23 16:34
 */
public class MessageVO {
    /**
     * 客户端和服务器之间的协议
     */
    private String type;
    /**
     * 客户端与服务端之间发送数据的内容
     */
    private String content;
    /**
     * 客户端告诉服务器要将信息发送给何处
     */
    private String to;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
