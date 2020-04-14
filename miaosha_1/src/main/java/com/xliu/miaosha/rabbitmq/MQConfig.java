package com.xliu.miaosha.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuxin
 * @version 1.0
 * @date 2020/4/14 10:26
 */
@Configuration
public class MQConfig {

    public static final String MIAOSHA_QUEUE = "miaosha.queue";


    @Bean
    public Queue queue(){
        return new Queue(MIAOSHA_QUEUE,true);
    }

}
