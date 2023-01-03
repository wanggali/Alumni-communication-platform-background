package com.pzhu.acp.email;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @Auther: gali
 * @Date: 2022-12-22 22:23
 * @Description:
 */
@SpringBootTest
public class MailProducerTest {
    @Resource
    private MailProducer mailProducer;

    @Test
    public void sendQQEmail(){
        mailProducer.sendEmail("2722933638@qq.com");
    }
}
