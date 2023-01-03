package com.pzhu.acp.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @Auther: gali
 * @Date: 2022-11-22 22:18
 * @Description:
 */
@SpringBootTest
public class TestOriginMapper {
    @Resource
    private OriginMapper originMapper;

    @Resource
    private UserMapper userMapper;

    @Test
    public void testSelectOriginById(){
//        System.out.println(originMapper);
//        System.out.println(originMapper.selectOriginById(2L));
        System.out.println(userMapper.selectUserById(2L));
    }
}
