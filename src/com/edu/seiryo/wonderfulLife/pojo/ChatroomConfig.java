package com.edu.seiryo.wonderfulLife.pojo;

public class ChatroomConfig {
    private int id;              // 配置ID（固定为1）
    private int isOpen;          // 手动模式下的开关状态：0=关闭, 1=开启
    private int manualMode;      // 模式：0=自动模式（按时间段）, 1=手动模式
    private String openTime;     // 自动模式开放时间（HH:mm）
    private String closeTime;    // 自动模式关闭时间（HH:mm）

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }

    public int getManualMode() {
        return manualMode;
    }

    public void setManualMode(int manualMode) {
        this.manualMode = manualMode;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }
}
