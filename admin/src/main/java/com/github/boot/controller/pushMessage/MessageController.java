package com.github.boot.controller.pushMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class MessageController {



    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;


    /**
     * 用于登录系统的群聊 [每个用户必须订阅]
     *
     * @param msg
     * @param stompHeaderAccessor
     * @return
     */
    @MessageMapping("/send_group_message") //群发消息路径,/app/send_group_message
    @SendTo("/topic/group_message")        //每个用户登录后订阅群消息 ：/topic/group_message
    public String groupMessage(String msg, StompHeaderAccessor stompHeaderAccessor) {
        Principal user = stompHeaderAccessor.getUser();
        String name = stompHeaderAccessor.getUser().getName(); //当前用户
        return name + ": " + msg;
    }

    /**
     * 系统主动推送的消息 [用于全员推送]
     * 用户订阅路径： /topic/system_notice
     */
   // @Scheduled(cron = "*/1 * * * * ?")

    /**
     * 将系统时间推送给，订阅过/topic/system_notice主题的用户
     */
    public void sendSystemMessage() {
        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sm.format(new Date());
        simpMessageSendingOperations.convertAndSend("/topic/system_notice", format);
    }


    /**
     * 处理点对点单聊
     *
     * @param userId
     * @param msg
     * @return
     */
    @MessageMapping("/send_message_to/{userId}")
    //@SendToUser 默认发送到 /user/queue/send_message_to
    @SendToUser("/single_message_list")  //指定的消息代理路径，订阅路径是 /user/queue/single_message_list
    // @SendToUser("/single_message_list")       //订阅路径是 /user/{当前用户}/single_message_list
    public String sendMessageToUser(@DestinationVariable String userId, String msg) {
        System.out.println(msg);
        simpMessageSendingOperations.convertAndSendToUser( userId,"/single_message_list", msg);
        return "c2c" + userId + msg;
    }


    //推送群聊在线用户数据
    //simpMessageSendingOperations.convertAndSend("/topic/group_online", format);
}
