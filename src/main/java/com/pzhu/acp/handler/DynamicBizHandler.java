package com.pzhu.acp.handler;

import com.pzhu.acp.enums.BizTypeEnum;

/**
 * @Auther: gali
 * @Date: 2023-04-24 22:40
 * @Description:
 */
public class DynamicBizHandler extends BizHandler {
    @Override
    public void afterPropertiesSet() throws Exception {
        BizHandlerRegister.register(BizTypeEnum.DYNAMIC.getType(), this);
    }
}
