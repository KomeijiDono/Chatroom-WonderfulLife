package com.edu.seiryo.wonderfulLife.pojo;

public class Chat {
    private int id;          // 消息ID（自增主键）
    private String info;     // 消息内容（支持HTML文本或img标签）
    private String time;     // 发送时间（yyyy-MM-dd HH:mm:ss）
    private String sender;   // 发送者账号
    private String color;    // 文字颜色（十六进制色值）
    private String nickname; // 发送者昵称（前端显示用）

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
