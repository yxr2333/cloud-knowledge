package com.sheep.cloud.config;

import com.sheep.cloud.common.CommonFields;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.config
 * @datetime 2022/9/23 星期五
 */
@Configuration
public class OrderQueueConfig {

    // 声明订单队列
    @Bean("orderQueue")
    public Queue orderQueue() {
        return new Queue(CommonFields.ORDER_QUEUE_NAME);
    }

    // 声明订单交换机
    @Bean("orderExchange")
    public CustomExchange orderExchange() {
        HashMap<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(CommonFields.ORDER_EXCHANGE_NAME, "x-delayed-message", true, false, args);
    }

    // 绑定订单队列和交换机
    @Bean
    public Binding bindingOrderQueue(@Qualifier("orderQueue") Queue queue,
                                     @Qualifier("orderExchange") CustomExchange customExchange) {
        return BindingBuilder.bind(queue).to(customExchange).with(CommonFields.ORDER_ROUTING_KEY).noargs();
    }
}
