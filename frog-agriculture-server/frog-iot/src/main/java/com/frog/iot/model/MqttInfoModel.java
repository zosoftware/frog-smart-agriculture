package com.frog.iot.model;

/**
 * id和name
 * 
 * @author kerwincui
 * @date 2021-12-16
 */
public class MqttInfoModel
{
    private String clientid;

    private String username;

    private String password;

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
