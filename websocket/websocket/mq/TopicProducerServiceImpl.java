package com.thinkgem.jeesite.activemq;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by SoleGlory on 2017/11/3.
 */
@Component
public class TopicProducerServiceImpl implements TopicProducerService {
    @Resource
    private JmsTemplate jmsTemplate;

    @Autowired
    @Qualifier("topicDestination")
    private Destination topicDestination;

    /**
     * 发送文本消息到指定主题
     * @param dest
     * @param msg
     */
    @Override
    public void sendTextMessage(Destination dest, final String msg) {
        jmsTemplate.send(dest, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(msg);
            }
        });
    }

    /**
     * 发送文本消息到指定主题
     * @param msg
     */
    @Override
    public void sendTextMessage(final String msg) {
        sendTextMessage(topicDestination,msg);
    }


    /**
     * 发送对象消息到指定主题
     * @param dest
     * @param obj
     */
    @Override
    public void setObjectMessage(Destination dest, final Serializable obj) {
        jmsTemplate.send(dest, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createObjectMessage(obj);
            }
        });
    }

    /**
     * 发送对象消息到指定主题
     * @param map
     */
    @Override
    public void setObjectMessage(Map<String, Object> map) {
        setObjectMessage(topicDestination, (Serializable) map);
    }

    /**
     * 发送对象消息到指定主题
     * @param list
     */
    @Override
    public void setObjectMessage(List list) {
        setObjectMessage(topicDestination, (Serializable) list);
    }
    /**
     * 发送对象消息到指定主题
     * @param serializable
     */
    @Override
    public void setObjectMessage(Serializable serializable) {
        setObjectMessage(topicDestination, serializable);
    }

    /**
     * 发送文本消息到指定主题
     * @param msg
     */
    @Override
    public void setTextMessage(String msg) {
        sendTextMessage(topicDestination, msg);
    }

    /**
     * 发送文本消息到指定主题
     * @param instruct
     * @param key
     */
    @Override
    public void setInstructMessage(String instruct, String key) {
        Map<String,Object> msgMap = Maps.newHashMap();
        msgMap.put("message", instruct);
        msgMap.put("currentUserId", key);
        setObjectMessage(topicDestination, (Serializable) msgMap);
    }
}
