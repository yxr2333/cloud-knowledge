package com.sheep.cloud;

import com.sheep.cloud.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud
 * @datetime 2022/8/15 星期一
 */
@SpringBootTest(classes = UserApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ApplicationTest {

    @Autowired
    private UserService userService;

    @Test
    public void contextLoads() {
        List<Integer> labels1 = Arrays.asList(1);
        List<Integer> labels2 = Arrays.asList(1, 2, 3, 4);
        List<Integer> labels3 = Arrays.asList(4);
        List<Integer> labels4 = Arrays.asList(2, 3);
        System.out.println(userService.findAllByLabelId(labels1, 0, 10).data);
        System.out.println(userService.findAllByLabelId(labels2, 0, 10).data);
        System.out.println(userService.findAllByLabelId(labels3, 0, 10).data);
        System.out.println(userService.findAllByLabelId(labels4, 0, 10).data);
    }
}
