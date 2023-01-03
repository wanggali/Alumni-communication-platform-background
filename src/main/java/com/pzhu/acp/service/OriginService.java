package com.pzhu.acp.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pzhu.acp.model.entity.Origin;
import com.pzhu.acp.model.query.DeleteOriginQuery;
import com.pzhu.acp.model.query.GetOriginQuery;
import com.pzhu.acp.model.vo.OriginVO;

import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【origin】的数据库操作Service
* @createDate 2022-11-13 21:25:08
*/
public interface OriginService extends IService<Origin> {

    Boolean addOrigin(Origin origin);

    Boolean updateOrigin(Origin origin);

    Boolean deleteOrigin(DeleteOriginQuery deleteOriginQuery);

    Map<String, Object> GetOriginByQueryAndPage(GetOriginQuery getOriginQuery);
}
