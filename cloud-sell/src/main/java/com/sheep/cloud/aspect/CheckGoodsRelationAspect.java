package com.sheep.cloud.aspect;

import com.sheep.cloud.dto.request.UpdateGoodsInfoParam;
import com.sheep.cloud.repository.IGoodsEntityRepository;
import com.sheep.cloud.repository.ISpikeDetailsEntityRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Created By IntelliJ IDEA
 *
 * @author IceCreamQAQ
 * @datetime 2022/11/2 星期三
 * Happy Every Coding Time~
 */

@Aspect
@Component
public class CheckGoodsRelationAspect {

    @Autowired
    private IGoodsEntityRepository goodsEntityRepository;

    @Autowired
    private ISpikeDetailsEntityRepository spikeDetailsEntityRepository;

    @Pointcut("@annotation(com.sheep.cloud.aspect.CheckGoodsRelation)")
    public void checkGoodsRelation() {
    }

    @Around("@annotation(checkGoodsRelation)")
    @Transactional(rollbackFor = Exception.class)
    public Object before(ProceedingJoinPoint point, CheckGoodsRelation checkGoodsRelation) throws Throwable {
        Object[] args = point.getArgs();
        Assert.isTrue(args.length > 0, "参数不能为空");
        UpdateGoodsInfoParam param = (UpdateGoodsInfoParam) args[0];
        Assert.notNull(param.getId(), "id不能为空");
        boolean flag = spikeDetailsEntityRepository.existsByGoodsId(param.getId());
        if (flag) {
            throw new RuntimeException("该商品已经被秒杀活动关联，无法修改");
        }
        return point.proceed();
    }
}
