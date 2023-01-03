package com.pzhu.acp.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pzhu.acp.model.entity.OriginUser;
import com.pzhu.acp.model.query.DeleteOriginUserQuery;
import com.pzhu.acp.model.query.GetOriginUserQuery;

import java.util.Map;

/**
* @author Administrator
* @description 针对表【origin_user】的数据库操作Service
* @createDate 2022-11-21 21:34:22
*/
public interface OriginUserService extends IService<OriginUser> {

    Boolean addOriginUser(OriginUser originUser);

    Boolean deleteOriginUser(DeleteOriginUserQuery deleteOriginUserQuery);

    Map<String, Object> getOriginUsers(GetOriginUserQuery getOriginUserQuery);
}
