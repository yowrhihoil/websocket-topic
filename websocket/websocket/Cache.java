package com.thinkgem.jeesite.websocket;

import javax.websocket.Session;
import java.io.Serializable;
import javax.websocket.RemoteEndpoint.Basic;

/**
 * Created by SoleGlory on 2017/11/2.
 */
public class Cache implements Serializable{
    private static final long serialVersionUID = 1L;

    private Basic basic;

    //当前用户id
    private String currentUserId;

    public Basic getBasic() {
        return basic;
    }

    public void setBasic(Basic basic) {
        this.basic = basic;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }
}
