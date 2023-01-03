package com.pzhu.acp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pzhu.acp.model.entity.Push;
import com.pzhu.acp.model.query.GetPushByPageQuery;

import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【push】的数据库操作Service
* @createDate 2022-12-20 17:54:36
*/
public interface PushService extends IService<Push> {

    Boolean addPush(Push push);

    Boolean updatePush(Push push);

    Boolean deletePush(List<Long> ids);

    Map<String, Object> getPushInfoByPageOrParam(GetPushByPageQuery getPushByPageQuery);
}
