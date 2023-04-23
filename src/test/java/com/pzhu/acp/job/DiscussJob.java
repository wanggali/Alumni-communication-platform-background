package com.pzhu.acp.job;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @Auther: gali
 * @Date: 2023-04-12 23:14
 * @Description:
 */
@SpringBootTest
public class DiscussJob {
    @Resource
    private DiscussUpNumJob job;
    @Test
    public void test(){
        job.addThumbUpJob();
    }
}
