package com.thinkgem.jeesite.activemq;

import javax.jms.Destination;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by SoleGlory on 2017/11/3.
 */
public interface TopicProducerService {
    /**
     * 发送文本消息到指定主题
     * @param dest
     * @param msg
     */
    void sendTextMessage(Destination dest, final String msg);


    /**
     * 发送文本消息到指定主题
     * @param msg
     */
    public void sendTextMessage(final String msg);

    /**
     * 发送对象消息到指定主题
     * @param dest
     * @param obj
     */
    void setObjectMessage(Destination dest, final Serializable obj);

    /**
     * 发送对象消息到指定主题
     * @param map
     */
    void setObjectMessage(Map<String, Object> map);

    /**
     * 发送对象消息到指定主题
     * @param serializable
     */
    void setObjectMessage(Serializable serializable);


    /**
     * 发送对象消息到指定主题
     * @param serializable
     */
    void setObjectMessage(List serializable);
    /**
     * 发送文本消息到指定主题
     * @param msg
     */
    void setTextMessage(String msg);

    /**
     * 发送id，指令到指定主题
     * @param instruct
     * @param id
     */
    void setInstructMessage(String instruct, String id);
}
