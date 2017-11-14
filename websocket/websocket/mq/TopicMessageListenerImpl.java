package com.thinkgem.jeesite.activemq;

import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.websocket.WebSocketManage;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import java.util.Map;

/**
 * Created by SoleGlory on 2017/11/3.
 */
public class TopicMessageListenerImpl implements MessageListener {

    @SuppressWarnings("unchecked")
    @Override
    public void onMessage(Message message) {

        System.out.println("======================ActiveMq topic监听======================");

        ObjectMessage obj = (ObjectMessage) message;
        try {
            Map<String,String> map = (Map<String,String>)obj.getObject();
            String currentUserId = map.get("currentUserId");
            if(StringUtils.isNoneEmpty(currentUserId)){
                WebSocketManage.sendMessage(map.get("message"), currentUserId);
            }else{
                WebSocketManage.sendMessageAll(map.get("message"));
            }
        } catch (JMSException e) {
            // 执行失败以后修改队列状态为失败
            e.printStackTrace();
        }
    }

}
