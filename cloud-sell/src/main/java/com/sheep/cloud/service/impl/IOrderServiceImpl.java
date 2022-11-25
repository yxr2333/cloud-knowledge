package com.sheep.cloud.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.extra.mail.MailUtil;
import com.sheep.cloud.common.CommonFields;
import com.sheep.cloud.common.OrderStatusEnum;
import com.sheep.cloud.common.SafeAccountHistoryEnum;
import com.sheep.cloud.dao.sell.*;
import com.sheep.cloud.dto.request.sell.*;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.entity.sell.*;
import com.sheep.cloud.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.service.impl
 * @datetime 2022/9/23 星期五
 */
@Service
@Slf4j
public class IOrderServiceImpl implements IOrderService {
    @Autowired
    private ISellGoodsEntityRepository goodsEntityRepository;
    @Autowired
    private ISellUserEntityRepository userEntityRepository;
    @Autowired
    private ISellOrdersEntityRepository ordersEntityRepository;
    @Autowired
    private ISellSafeAccountHistoryEntityRepository safeAccountRepository;
    @Autowired
    private ISellRefundOrderHistoryEntityRepository refundOrderHistoryEntityRepository;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private ISellUserEntity safeAccount;

    @PostConstruct
    public void init() {
        safeAccount = userEntityRepository.findById(1).orElse(null);
    }

    /**
     * 创建一个普通交易的订单
     *
     * @param param 订单信息
     * @return 创建结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> createCommonOrder(CreateCommonOrderParam param) {
        ISellGoodsEntity goodsEntity = goodsEntityRepository.findById(param.getGoodsId())
                .orElseThrow(() -> new RuntimeException("商品不存在"));
        ISellUserEntity buyerEntity = userEntityRepository.findById(param.getBuyerId())
                .orElseThrow(() -> new RuntimeException("买家不存在"));
        ISellUserEntity sellerEntity = userEntityRepository.findById(param.getSellerId())
                .orElseThrow(() -> new RuntimeException("卖家不存在"));
        Integer freeTotal = goodsEntity.getFreeTotal();
        // 判断库存是否充足，以及商品是否打折
        if (freeTotal <= 0) {
            return new ApiResult<>().error("商品库存不足");
        }
        if (goodsEntity.getIsDown()) {
            return new ApiResult<>().error("商品已下架，无法购买");
        }
        if (!goodsEntity.getIsDiscount()) {
            goodsEntity.setDiscountPercent(null);
        } else {
            Double percent = goodsEntity.getDiscountPercent();
            if (percent == null || percent <= 0 || percent >= 1) {
                throw new RuntimeException("折扣率不合法");
            }
        }
        ISellOrdersEntity entity = ISellOrdersEntity.builder()
                .oid(IdUtil.randomUUID())
                .good(goodsEntity)
                .price(goodsEntity.getPrice())
                .isDiscount(goodsEntity.getIsDiscount())
                .discountPercent(goodsEntity.getDiscountPercent())
                // 标记为普通交易订单
                .sellType(0)
                // 计算最后的价格
                .finalPrice(goodsEntity.getDiscountPercent() == null ? goodsEntity.getPrice() : goodsEntity.getPrice() * goodsEntity.getDiscountPercent())
                .seller(sellerEntity)
                .sellerName(sellerEntity.getUsername())
                .sellerMail(sellerEntity.getEmail())
                .buyer(buyerEntity)
                .buyerName(buyerEntity.getUsername())
                .buyerMail(buyerEntity.getEmail())
                .orderStatus(OrderStatusEnum.NOT_PAYED.code)
                .orderStatusDescription(OrderStatusEnum.NOT_PAYED.description)
                .createTime(LocalDateTime.now())
                .build();
        ISellOrdersEntity order = ordersEntityRepository.save(entity);
        synchronized (this) {
            goodsEntity.setFreeTotal(freeTotal - 1);
            goodsEntityRepository.save(goodsEntity);
        }
        log.info("订单创建成功，主键：{}，订单号：{}", order.getId(), entity.getOid());
        rabbitTemplate.convertAndSend(CommonFields.ORDER_EXCHANGE_NAME, CommonFields.ORDER_ROUTING_KEY, String.valueOf(order.getId()), message -> {
            // 设置延时时间十分钟
            message.getMessageProperties().setDelay(10 * 60 * 1000);
//            message.getMessageProperties().setDelay(10 * 1000);
            log.info("向延时队列发送消息成功，订单id：{}", order.getId());
            return message;
        });
        return new ApiResult<>().success("订单创建成功，订单号：" + order.getOid());
    }

    /**
     * 支付订单
     *
     * @param param 订单信息
     * @return 支付结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> payOrder(PayOrderParam param) {
        ISellOrdersEntity order = ordersEntityRepository
                .findByOid(param.getOrderId())
                .orElseThrow(() -> new RuntimeException("订单不存在"));
        ISellUserEntity buyer = userEntityRepository
                .findById(param.getBuyerId())
                .orElseThrow(() -> new RuntimeException("买家不存在"));
        if (!order.getOrderStatus().equals(OrderStatusEnum.NOT_PAYED.code)) {
            return new ApiResult<>().error("订单状态异常");
        }
        if (order.getGood().getIsDown()) {
            order.setOrderStatus(OrderStatusEnum.GOODS_DOWN_CANCELED.code);
            order.setOrderStatusDescription(OrderStatusEnum.GOODS_DOWN_CANCELED.description);
            order.setFinishTime(LocalDateTime.now());
            return new ApiResult<>().error("商品已下架，订单已取消，支付失败");
        }
        if (!order.getBuyer().getId().equals(buyer.getId())) {
            return new ApiResult<>().error("这不是您的订单~");
        }
        if (order.getFinalPrice() > buyer.getFreeMoney()) {
            return new ApiResult<>().error("余额不足");
        }
        // 扣费
        buyer.setFreeMoney(buyer.getFreeMoney() - order.getFinalPrice());
        userEntityRepository.save(buyer);

        // 钱转入安全账户
        if (safeAccount == null) {
            throw new RuntimeException("暂时无法支付，请稍后再试");
        }
        safeAccount.setFreeMoney(safeAccount.getFreeMoney() + order.getFinalPrice());
        userEntityRepository.save(safeAccount);

        // 添加一条安全账户的转入流水
        ISellSafeAccountHistoryEntity safeAccountHistory = ISellSafeAccountHistoryEntity.builder()
                .orderId(order.getId())
                .price(order.getFinalPrice())
                .transType(SafeAccountHistoryEnum.TRANS_IN.type)
                .transReason(SafeAccountHistoryEnum.TRANS_IN.reason)
                .transTime(LocalDateTime.now())
                .build();
        safeAccountRepository.save(safeAccountHistory);

        // 支付成功，修改订单状态
        order.setOrderStatus(OrderStatusEnum.PAYED_NOT_CHECKED.code);
        order.setOrderStatusDescription(OrderStatusEnum.PAYED_NOT_CHECKED.description);
        order.setPayTime(LocalDateTime.now());
        ordersEntityRepository.save(order);
        return new ApiResult<>().success("支付成功");
    }


    /**
     * 取消订单
     *
     * @param param 订单信息
     * @return 取消结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> cancelOrder(CancelOrderParam param) {
        ISellOrdersEntity order = ordersEntityRepository
                .findByOid(param.getOrderId())
                .orElseThrow(() -> new RuntimeException("未查找到订单信息"));
        ISellUserEntity submitUser = userEntityRepository
                .findById(param.getSubmitUserId())
                .orElseThrow(() -> new RuntimeException("未查找到用户信息"));
        if (!order.getBuyer().getId().equals(submitUser.getId())) {
            return new ApiResult<>().error("这不是您的订单~");
        }
        if (order.getOrderStatus().equals(OrderStatusEnum.REFUNDED.code)
                || order.getOrderStatus().equals(OrderStatusEnum.CANCELED.code)) {
            return new ApiResult<>().error("订单" + order.getOrderStatusDescription() + "，无法取消");
        }
        if (order.getOrderStatus().equals(OrderStatusEnum.DELIVERED_NOT_RECEIVED.code)
                || order.getOrderStatus().equals(OrderStatusEnum.FINISHED.code)) {
            return new ApiResult<>().error("订单已发货，无法取消");
        }
        order.setOrderStatus(OrderStatusEnum.CANCELED.code);
        order.setOrderStatusDescription(OrderStatusEnum.CANCELED.description);
        order.setFinishTime(LocalDateTime.now());
        ordersEntityRepository.save(order);

        // 释放库存以及退款给买家
        ISellGoodsEntity goods = goodsEntityRepository
                .findById(order.getGood().getId())
                .orElseThrow(() -> new RuntimeException("未查找到商品信息"));
        synchronized (this) {
            goods.setFreeTotal(goods.getFreeTotal() + 1);
            submitUser.setFreeMoney(submitUser.getFreeMoney() + order.getFinalPrice());
            safeAccount.setFreeMoney(safeAccount.getFreeMoney() - order.getFinalPrice());
        }
        goodsEntityRepository.save(goods);
        userEntityRepository.save(submitUser);
        userEntityRepository.save(safeAccount);

        // 添加一条安全账户的转出流水
        ISellSafeAccountHistoryEntity safeAccountHistory = ISellSafeAccountHistoryEntity.builder()
                .transTime(LocalDateTime.now())
                .price(order.getFinalPrice())
                .transType(SafeAccountHistoryEnum.TRANS_OUT_TO_BUYER.type)
                .transReason(SafeAccountHistoryEnum.TRANS_OUT_TO_BUYER.reason)
                .orderId(order.getId())
                .build();
        safeAccountRepository.save(safeAccountHistory);
        return new ApiResult<>().success("取消成功");
    }

    /**
     * 审核订单是否支付成功
     *
     * @param orderId 订单id
     * @return 审核结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> checkPay(String orderId) {
        ISellOrdersEntity order = ordersEntityRepository
                .findByOid(orderId)
                .orElseThrow(() -> new RuntimeException("未查找到订单信息"));
        order.setOrderStatus(OrderStatusEnum.CHECKED_NOT_DELIVERED.code);
        order.setOrderStatusDescription(OrderStatusEnum.CHECKED_NOT_DELIVERED.description);
        ordersEntityRepository.save(order);

        return new ApiResult<>().success(String.format("订单号:%s，已审核成功", orderId));
    }

    /**
     * 卖家发货
     *
     * @param orderId 订单id
     * @return 发货结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> deliverOrder(String orderId) {
        ISellOrdersEntity order = ordersEntityRepository
                .findByOid(orderId)
                .orElseThrow(() -> new RuntimeException("未查找到订单信息"));
        if (order.getOrderStatus().equals(OrderStatusEnum.PAYED_NOT_CHECKED.code)) {
            return new ApiResult<>().error("请等待管理员审核是否支付成功");
        }
        if (order.getOrderStatus().equals(OrderStatusEnum.DELIVERED_NOT_RECEIVED.code)) {
            return new ApiResult<>().error("订单已发货，无需重复发货");
        }
        if (!order.getOrderStatus().equals(OrderStatusEnum.CHECKED_NOT_DELIVERED.code)) {
            return new ApiResult<>().error("订单状态异常，无法发货");
        }
        order.setOrderStatus(OrderStatusEnum.DELIVERED_NOT_RECEIVED.code);
        order.setOrderStatusDescription(OrderStatusEnum.DELIVERED_NOT_RECEIVED.description);
        ordersEntityRepository.save(order);
        String content = String.format("尊敬的：%s，您订单号:%s，的商品已发货，请注意查收~", order.getBuyerName(), orderId);
        try {
            MailUtil.send(order.getBuyerMail(), "订单发货通知", content, false);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResult<>().error("暂时无法进行发货，请稍后再试");
        }
        return new ApiResult<>().success(String.format("订单号:%s，已发货", orderId));
    }

    /**
     * 买家确认收货
     *
     * @param orderId 订单id
     * @return 确认收货结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> checkSaveGoods(String orderId) {
        ISellOrdersEntity order = ordersEntityRepository.findByOid(orderId)
                .orElseThrow(() -> new RuntimeException("未查找到订单信息"));
        order.setOrderStatus(OrderStatusEnum.FINISHED.code);
        order.setOrderStatusDescription(OrderStatusEnum.FINISHED.description);
        order.setFinishTime(LocalDateTime.now());
        ordersEntityRepository.save(order);

        // 确认收货后，钱从安全账户转入卖家账户
        ISellUserEntity seller = order.getSeller();
        synchronized (this) {
            seller.setFreeMoney(seller.getFreeMoney() + order.getFinalPrice());
            safeAccount.setFreeMoney(safeAccount.getFreeMoney() - order.getFinalPrice());
        }
        userEntityRepository.save(seller);
        userEntityRepository.save(safeAccount);

        // 添加一条安全账户的转出流水
        ISellSafeAccountHistoryEntity safeAccountHistory = ISellSafeAccountHistoryEntity.builder()
                .transTime(LocalDateTime.now())
                .price(order.getFinalPrice())
                .transType(SafeAccountHistoryEnum.TRANS_OUT_TO_SELLER.type)
                .transReason(SafeAccountHistoryEnum.TRANS_OUT_TO_SELLER.reason)
                .orderId(order.getId())
                .build();
        safeAccountRepository.save(safeAccountHistory);
        String content = String.format("尊敬的：%s，您订单号:%s，的商品已被买家确认收货，交易成功。钱已转入您平台账号，请注意查收~", order.getSellerName(), orderId);
        try {
            MailUtil.send(order.getSellerMail(), "订单交易成功通知", content, false);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResult<>().error("暂时无法进行操作，请稍后再试");
        }
        return new ApiResult<>().success(String.format("订单号:%s，已确认收货", orderId));
    }

    /**
     * 申请退款
     *
     * @param param 申请退款信息
     * @return 申请退款结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> applyForRefund(ReplyRefundOrderParam param) {
        ISellOrdersEntity order = ordersEntityRepository.findByOid(param.getOrderId())
                .orElseThrow(() -> new RuntimeException("未查找到订单信息"));
        // 当卖家已发货，买家未确认收货的时候，才能申请退款
        if (!order.getOrderStatus().equals(OrderStatusEnum.DELIVERED_NOT_RECEIVED.code)) {
            return new ApiResult<>().error("当前订单状态不允许申请退款");
        }
        order.setOrderStatus(OrderStatusEnum.APPLY_NOT_REFUNDED.code);
        order.setOrderStatusDescription(OrderStatusEnum.APPLY_NOT_REFUNDED.description);

        ISellRefundOrderHistoryEntity history = ISellRefundOrderHistoryEntity.builder()
                .order(order)
                .createTime(LocalDateTime.now())
                .refundReason(param.getReason())
                .build();

        // 保存退款记录，并更新订单的状态
        refundOrderHistoryEntityRepository.save(history);
        ordersEntityRepository.save(order);

        // 给卖家发送邮件
        String content = String.format("尊敬的：%s，您订单号:%s，的商品已被买家申请退款，请尽快查阅处理~", order.getSellerName(), param.getOrderId());
        MailUtil.send(order.getSellerMail(), "订单申请退款通知", content, false);

        return new ApiResult<>().success(String.format("订单号:%s，已申请退款，等待卖家处理", param.getOrderId()));
    }

    /**
     * 卖家审核退款
     *
     * @param param 审核退款信息
     * @return 审核退款结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> checkRefund(CheckRefundOrderParam param) {
        ISellOrdersEntity order = ordersEntityRepository.findByOid(param.getOrderId())
                .orElseThrow(() -> new RuntimeException("未查找到订单信息"));
        ISellRefundOrderHistoryEntity history =
                refundOrderHistoryEntityRepository.getOne(param.getRefundHistoryId());
        // 同意退款，钱从安全账户转入买家账户
        if (param.getIsAgree().equals(true)) {
            // 更新订单状态
            order.setOrderStatus(OrderStatusEnum.REFUNDED.code);
            order.setOrderStatusDescription(OrderStatusEnum.REFUNDED.description);
            // 退款之后，设置订单的结束时间
            order.setFinishTime(LocalDateTime.now());
            ordersEntityRepository.save(order);
            history.setFinalRefundTime(LocalDateTime.now());
            history.setIsRefunded(true);
            refundOrderHistoryEntityRepository.save(history);
            synchronized (this) {
                safeAccount.setFreeMoney(safeAccount.getFreeMoney() - order.getFinalPrice());
                order.getBuyer().setFreeMoney(order.getBuyer().getFreeMoney() + order.getFinalPrice());
                userEntityRepository.save(safeAccount);
                userEntityRepository.save(order.getBuyer());
            }
            // 保存安全账户历史记录
            ISellSafeAccountHistoryEntity safeAccountHistory = ISellSafeAccountHistoryEntity.builder()
                    .transTime(LocalDateTime.now())
                    .price(order.getFinalPrice())
                    .transType(SafeAccountHistoryEnum.TRANS_OUT_TO_BUYER.type)
                    .transReason(SafeAccountHistoryEnum.TRANS_OUT_TO_BUYER.reason)
                    .orderId(order.getId())
                    .build();
            safeAccountRepository.save(safeAccountHistory);
            // 给买家发送邮件
            String content = String.format("尊敬的：%s，您订单号:%s，的商品已被卖家同意退款，退款金额为：%s元，已转入您的账户~",
                    order.getBuyerName(), param.getOrderId(), order.getFinalPrice());
            MailUtil.send(order.getBuyerMail(), "订单退款通知", content, false);
            return new ApiResult<>().success(String.format("订单号:%s，已退款", param.getOrderId()));
        } else {
            // 不同意退款，更新订单状态
            order.setOrderStatus(OrderStatusEnum.DELIVERED_NOT_RECEIVED.code);
            order.setOrderStatusDescription(OrderStatusEnum.DELIVERED_NOT_RECEIVED.description);
            ordersEntityRepository.save(order);
            history.setFinalRefundTime(LocalDateTime.now());
            history.setIsRefunded(false);
            history.setRefusedRefundReason(param.getRefusedReason());
            refundOrderHistoryEntityRepository.save(history);
            // 给买家发送邮件
            String content = String.format("尊敬的：%s，您订单号:%s，的商品已被卖家拒绝退款，如有疑问，请联系卖家~",
                    order.getBuyerName(), param.getOrderId());
            MailUtil.send(order.getBuyerMail(), "订单退款通知", content, false);
            return new ApiResult<>().success(String.format("订单号:%s，已拒绝退款", param.getOrderId()));
        }
    }
}
