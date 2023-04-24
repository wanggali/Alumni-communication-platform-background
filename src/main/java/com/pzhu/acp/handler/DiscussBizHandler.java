package com.pzhu.acp.handler;

import com.pzhu.acp.enums.BizTypeEnum;

/**
 * @Auther: gali
 * @Date: 2023-04-24 22:38
 * @Description:
 */
public class DiscussBizHandler extends BizHandler {
    @Override
    public void afterPropertiesSet() throws Exception {
        BizHandlerRegister.register(BizTypeEnum.DISCUSS.getType(), this);
    }
}
