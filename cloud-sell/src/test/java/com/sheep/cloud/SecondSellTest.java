package com.sheep.cloud;

import com.sheep.cloud.dto.request.knowledge.IUsersLoginVO;
import com.sheep.cloud.service.IGoodsService;
import com.sheep.cloud.service.IUserInfoService;
import com.sheep.cloud.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud
 * @datetime 2022/10/10 星期一
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class SecondSellTest {

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IUserInfoService userInfoService;

    @Test
    public void testFindGoodsDetail() {
        goodsService.findGoodsDetail(1);
    }

    @Test
    public void testFindAllGoods() {
        int pageNum = 1;
        int pageSize = 10;
        PageRequest pageable = PageRequest.of(pageNum - 1, pageSize);
        System.out.println(goodsService.findAllGoods(pageable));
    }


    @Test
    public void testFindAllByReleaseUserId() {
        int pageNum = 1;
        int pageSize = 10;
        PageRequest pageable = PageRequest.of(pageNum - 1, pageSize);
        System.out.println(goodsService.findAllGoodsByUserId(pageable, 2));
    }

    @Test
    public void testUserLogin() {
        IUsersLoginVO param = new IUsersLoginVO("yxr", "123456");
        System.out.println(userService.doLogin(param));
    }

    @Test
    public void testFindUserByUserId() {
        System.out.println(userInfoService.findUserInfoDetail(1));
    }
}
