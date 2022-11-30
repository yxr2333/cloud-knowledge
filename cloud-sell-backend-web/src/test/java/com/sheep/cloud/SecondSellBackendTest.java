package com.sheep.cloud;

import com.sheep.cloud.entity.sell.ISellGoodsEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

/**
 * Created By IntelliJ IDEA
 *
 * @author IceCreamQAQ
 * @datetime 2022/11/27 星期日
 * Happy Every Coding Time~
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class SecondSellBackendTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional(rollbackFor = Exception.class)
    public void test() {
        System.out.println("=======================================================");
        System.out.println("=======================================================");
        entityManager.createNamedQuery("findShoppingCartGoodsByUid", ISellGoodsEntity.class)
                .setParameter(1, 1)
                .setParameter(2, 2)
                .setParameter(3, 1)
                .getResultList()
                .forEach(System.out::println);
        System.out.println("=======================================================");
        System.out.println("=======================================================");
    }
}
