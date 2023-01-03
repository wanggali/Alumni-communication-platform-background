package com.pzhu.acp.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pzhu.acp.model.vo.OriginUserVO;
import org.joda.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Calendar;

/**
 * @Auther: gali
 * @Date: 2022-11-22 22:18
 * @Description:
 */
@SpringBootTest
public class TestOriginUserMapper {
    @Resource
    private OriginUserMapper originUserMapper;

    @Test
    public void testSelectOriginById(){
        Page<OriginUserVO> originUserVOPage = new Page<>(1,5);
        System.out.println(originUserMapper.selectOriginUserVO(originUserVOPage,3L));
    }
}
