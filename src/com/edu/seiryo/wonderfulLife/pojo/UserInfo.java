package com.edu.seiryo.wonderfulLife.pojo;

public class UserInfo {
    private String account;     // 账号（主键）
    private String password;    // 密码
    private int type;           // 用户类型：0=普通用户, 1=管理员
    private int online;         // 在线状态：0=离线, 1=在线
    private String sex;         // 性别：男/女
    private String nickname;    // 昵称

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
