package com.github.boot.configure;

import com.github.boot.interceptor.WebSocketHandshakeInterceptor;
import com.sun.security.auth.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ImmutableMessageChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import java.security.Principal;

@Configuration
@EnableWebSocketMessageBroker
public class SocketConfig extends AbstractWebSocketMessageBrokerConfigurer  {


   @Autowired
   private WebSocketHandshakeInterceptor webSocketHandshakeInterceptor;




    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/message").addInterceptors(webSocketHandshakeInterceptor)
                .setAllowedOrigins("*").withSockJS();
    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
      /*  registry.addDecoratorFactory(new WebSocketHandlerDecoratorFactory() {
            @Override
            public WebSocketHandler decorate(final WebSocketHandler handler) {
                return new WebSocketHandlerDecorator(handler) {
                    @Override
                    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
                        // 客户端与服务器端建立连接后，此处记录谁上线了
                        String username = session.getPrincipal().getName();
                        super.afterConnectionEstablished(session);
                    }
                    @Override
                    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
                        // 客户端与服务器端断开连接后，此处记录谁下线了
                        String username = session.getPrincipal().getName();
                        super.afterConnectionClosed(session, closeStatus);
                    }
                };
            }
        });*/
        super.configureWebSocketTransport(registry);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ImmutableMessageChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                //1、判断是否首次连接
                if (StompCommand.CONNECT.equals(accessor.getCommand())){
                    //2、判断用户名和密码
                    String token = accessor.getNativeHeader("authorization").get(0);
                    if(StringUtils.isEmpty(token)) return null;
                    Principal principal = new UserPrincipal("admin");
                    accessor.setUser(principal);
                    return message;
                }
                return message;
                //不是首次连接，已经登陆成功
            }
        });
        super.configureClientInboundChannel(registration);
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 启用了 STOMP 代理中继功能，并将其代理目的地前缀设置为 /topic and /queue
        registry.enableSimpleBroker("/topic","/message");
        // 应用程序目的前缀
        //例如客户端发送消息的目的地为/app/send，则对应控制层@MessageMapping(“/send”)
        //客户端订阅主题的目的地为/app/subscribe，则对应控制层@SubscribeMapping(“/subscribe”)
        registry.setApplicationDestinationPrefixes("/app");
        //点对点使用的订阅前缀（客户端订阅路径上会体现出来），不设置的话，默认也是/user/
         //registry.setUserDestinationPrefix("/user");

        //默认情况下： STOMP 代理中继会假设 代理监听 localhost 的61613 端口，
        // 并且 client 的 username 和password 均为 guest。当然你也可以自行定义

        super.configureMessageBroker(registry);
    }
}
