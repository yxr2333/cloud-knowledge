package com.sheep.cloud.mq;

import cn.hutool.extra.mail.MailUtil;
import com.sheep.cloud.common.CommonFields;
import com.sheep.cloud.common.OrderStatusEnum;
import com.sheep.cloud.entity.sell.ISellGoodsEntity;
import com.sheep.cloud.entity.sell.ISellOrdersEntity;
import com.sheep.cloud.entity.sell.ISellWishBuyEntity;
import com.sheep.cloud.dao.sell.ISellGoodsEntityRepository;
import com.sheep.cloud.dao.sell.ISellOrdersEntityRepository;
import com.sheep.cloud.dao.sell.ISellWishBuyEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.mq
 * @datetime 2022/9/23 星期五
 */
@Slf4j
@Component
public class DeadOrderQueueConsumer {

    @Autowired
    private ISellOrdersEntityRepository ordersEntityRepository;
    @Autowired
    private ISellGoodsEntityRepository goodsEntityRepository;

    @Autowired
    private ISellWishBuyEntityRepository wishBuyEntityRepository;

    @RabbitListener(queues = CommonFields.ORDER_QUEUE_NAME)
    @Transactional(rollbackFor = Exception.class)
    public void orderOverTime(Message message) {
        String oid = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("订单延时队列收到消息，订单主键id：{}", oid);
        Optional<ISellOrdersEntity> optional =
                ordersEntityRepository.findById(Integer.valueOf(oid));
        assert optional.isPresent();
        ISellOrdersEntity order = optional.get();
        if (order.getOrderStatus().equals(OrderStatusEnum.NOT_PAYED.code)) {
            // 修改订单状态为已取消
            order.setOrderStatus(OrderStatusEnum.CANCELED.code);
            order.setOrderStatusDescription("订单超时未支付,自动取消");
            ordersEntityRepository.save(order);
            // 修改商品库存
            synchronized (this) {
                ISellGoodsEntity goods = order.getGood();
                Integer freeTotal = goods.getFreeTotal();
                goods.setFreeTotal(freeTotal + 1);
                goodsEntityRepository.save(goods);
            }
            String content = "尊敬的：" + order.getBuyerName() + "您好！您的订单：" + order.getOid() + "在规定时间内未及时支付，已经自动取消";
            MailUtil.send(order.getBuyerMail(), CommonFields.ORDER_OVERTIME_MAIL_TITLE, content, false);
        }
    }

    @RabbitListener(queues = CommonFields.WISH_BUY_QUEUE_NAME)
    @Transactional(rollbackFor = Exception.class)
    public void wishBuyNoReply(Message message) {
        String wishBuyId = new String(message.getBody(), StandardCharsets.UTF_8);
        log.info("求购信息延时队列收到消息，求购编号：{}", wishBuyId);
        ISellWishBuyEntity wishBuy =
                wishBuyEntityRepository.getOne(Integer.valueOf(wishBuyId));
        if (wishBuy.getIsFinished().equals(Boolean.FALSE)) {
            // 设置求购信息为已结束、已下架
            wishBuy.setIsDown(true);
            wishBuy.setIsFinished(true);
            wishBuy.setFinishTime(LocalDateTime.now());
            wishBuyEntityRepository.save(wishBuy);
            // 发送邮件通知发布者
            String content = String.format("<div><div>尊敬的：%s您好！您发布的求购信息：%s在规定时间内未有人回复，已经自动结束</div><img src=\"%s\" /></div>", wishBuy.getPubUser().getUsername(), wishBuy.getDescription(), wishBuy.getImgUrl());
            MailUtil.send(wishBuy.getPubUser().getEmail(), "求购超时自动取消通知", content, true);
        }
    }
}
