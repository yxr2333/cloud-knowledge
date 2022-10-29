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
 * @datetime 2022/10/10 星期一
 */
@Configuration
public class WishBuyQueueConfig {

    @Bean("wishBuyQueue")
    public Queue wishBuyQueue() {
        return new Queue(CommonFields.WISH_BUY_QUEUE_NAME);
    }

    @Bean("wishBuyExchange")
    public CustomExchange wishBuyExchange() {
        HashMap<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(CommonFields.WISH_BUY_EXCHANGE_NAME, "x-delayed-message", true, false, args);
    }

    @Bean
    public Binding bindingWishBuyQueue(@Qualifier("wishBuyQueue") Queue queue,
                                       @Qualifier("wishBuyExchange") CustomExchange customExchange) {
        return BindingBuilder.bind(queue).to(customExchange).with(CommonFields.WISH_BUY_ROUTING_KEY).noargs();
    }
}
