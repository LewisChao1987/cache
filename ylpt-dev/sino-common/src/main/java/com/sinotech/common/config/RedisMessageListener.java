package com.sinotech.common.config;

import com.sinotech.common.consts.DataConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * @author 郭训民
 * @description: redis消息队列配置-订阅者
 * @title: RedisMessageListener
 * @projectName ylpt-dev
 * @date 2022/2/12 14:06
 */
@Configuration
public class RedisMessageListener {

    @Autowired
    private RedisSubscriber subscriber;

    /**
     * 充当消息侦听器容器
     * 创建连接工厂
     * @param connectionFactory
     * @param listenerAdapter
     * @return
     */
    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                                   MessageListenerAdapter listenerAdapter, MessageListenerAdapter listenerAdapterTest2){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //接受消息的key
        container.addMessageListener(listenerAdapter,new PatternTopic(DataConstants.RedisTopic.REDIS_TOPIC_WS));
        return container;
    }

    /**
     * 绑定消息监听者和接收监听的方法
     * @param receiver
     * @return
     */
    @Bean
    public MessageListenerAdapter listenerAdapter(){
        return new MessageListenerAdapter(subscriber);
    }
}
