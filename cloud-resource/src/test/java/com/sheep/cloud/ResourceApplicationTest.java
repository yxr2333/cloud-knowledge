package com.sheep.cloud;

import com.sheep.cloud.dao.IResourcesEntityRepository;
import com.sheep.cloud.service.ResourceService;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(classes = ResourceApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ResourceApplicationTest {
    @Autowired
    ResourceService resourceService;

    @Autowired
    IResourcesEntityRepository iResourcesEntityRepository;
   @Test
    public void test() {

//       System.out.println(resourceService.findAllByListIn(1));
       iResourcesEntityRepository.findAllByUserId(1).forEach(System.out::println);
   }

}
