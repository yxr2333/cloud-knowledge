package com.sheep.cloud;

import com.sheep.cloud.entity.knowledge.IWishesEntity;
import com.sheep.cloud.dto.response.PageData;
import com.sheep.cloud.service.WishService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud
 * @datetime 2022/8/16 星期二
 */
@SpringBootTest(classes = WishApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class CloudWishApplicationTest {

    @Autowired
    private WishService wishService;


    @Test
    public void contextLoad() {
        PageData<IWishesEntity> data = (PageData<IWishesEntity>) wishService.getWishList(0, 10).data;
        data.getData().forEach(System.out::println);
    }
}
