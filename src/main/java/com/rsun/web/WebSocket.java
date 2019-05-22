package com.rsun.web;

import com.google.gson.JsonObject;
import com.rsun.cache.CacheQueue;
import com.rsun.util.json.JsonParseUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/webSocket")
@Component
public class WebSocket {
    private static transient int onlineCount = 0;
    private static Map<String, WebSocket> clients = new ConcurrentHashMap<>();
    private Session session;
    private String id;
    @Autowired
    @Qualifier("reqBandLimit")
    private CacheQueue<UUID> reqBandLimit;

    @OnOpen
    public void onOpen(Session session) throws IOException {

        this.id = session.getId();
        this.session = session;

        addOnlineCount();
        clients.put(this.id, this);
        session.getBasicRemote().sendText("1");
        System.out.println("通道 " + id + " 打开");
    }

    @OnClose
    public void close(Session session) {
        String id = session.getId();
        clients.remove(id);
        subOnlineCount();
        System.out.println("通道 " + id + " 关闭");
    }

    @OnMessage
    public void onMessage(String message) throws IOException {

        JsonObject jsonTo = JsonParseUtil.convertJson(message);
        String mes = jsonTo.get("message").toString();

        if (!jsonTo.get("To").equals("All")) {
            sendMessageTo(mes, jsonTo.get("To").toString());
        } else {
            sendMessageAll("给所有人");
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public static void sendMessageTo(String message, String To) throws IOException {
        // session.getBasicRemote().sendText(message);
        //session.getAsyncRemote().sendText(message);
        for (WebSocket item : clients.values()) {
            if (item.id.equals(To))
                item.session.getAsyncRemote().sendText(message);
        }
    }

    public static void sendMessageAll(String message) {
        for (WebSocket item : clients.values()) {
            item.session.getAsyncRemote().sendText(message);
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocket.onlineCount--;
    }

    public static synchronized Map<String, WebSocket> getClients() {
        return clients;
    }

    public void sendReqBand() {
        WebSocket.sendMessageAll(reqBandLimit.size() + "");
    }
}