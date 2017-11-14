package com.thinkgem.jeesite.websocket;

import com.thinkgem.jeesite.common.utils.Collections3;
import com.thinkgem.jeesite.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * Created by SoleGlory on 2017/11/1.
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@ServerEndpoint(value="/websocket",configurator=HttpSessionConfigurator.class)
public class WebSocketManage {

    //日志
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketManage.class);

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteArraySet<Cache> webSocketSet = new CopyOnWriteArraySet<Cache>();

    //当前连接的缓存信息
    private Cache cache;

    /**
     * 连接建立成功调用的方法
     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session,EndpointConfig config){
        HttpSession httpSession= (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        if(httpSession != null){
            Cache cache = new Cache();
            cache.setBasic(session.getBasicRemote());
            cache.setCurrentUserId(String.valueOf(httpSession.getAttribute("currentUserId")));
            this.cache = cache;
            webSocketSet.add(cache);     //加入set中
        }
        System.out.println("有新连接加入！当前在线人数为:"+getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(){
        webSocketSet.remove(this.cache);  //从set中删除
        System.out.println("有一连接关闭！当前在线人数为:"+getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        LOG.info("来自客户端的消息:", message);
        //群发消息
        if(!Collections3.isEmpty(webSocketSet)){
            for(Cache item: webSocketSet){
                try {
                    executeSendMessage(message, item);
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }
    }

    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error){
        LOG.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     * @param message
     * @throws IOException
     */
    public static void executeSendMessage(String message, Cache cache) throws IOException{
        cache.getBasic().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }

    /**
     * 外部类对特定用户发送推送消息
     * @param message
     * @param userId
     */
    public static void sendMessage(String message, String userId){
        //单发消息
        if(!Collections3.isEmpty(webSocketSet)){
            for(Cache item: webSocketSet){
                if(StringUtils.isNoneEmpty(item.getCurrentUserId()) && item.getCurrentUserId().equals(userId)){
                    try {
                        executeSendMessage(message, item);
                    } catch (IOException e) {
                        e.printStackTrace();
                        LOG.error("用户【{}】,消息【{}】发送失败。", userId, message);
                        continue;
                    }
                }
            }
        }
    }

    /**
     * 外部类对所有在线对象发送消息
     * @param message
     */
    public static void sendMessageAll(String message){
        //群发消息
        if(!Collections3.isEmpty(webSocketSet)){
            for(Cache item: webSocketSet){
                if(StringUtils.isNoneEmpty(item.getCurrentUserId())) {
                    try {
                        executeSendMessage(message, item);
                    } catch (IOException e) {
                        e.printStackTrace();
                        LOG.error("用户【{}】,消息【{}】发送失败。", item.getCurrentUserId(), message);
                        continue;
                    }
                }
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return webSocketSet.size();
    }
}
