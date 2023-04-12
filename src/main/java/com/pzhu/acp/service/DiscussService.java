package com.pzhu.acp.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pzhu.acp.common.BaseResponse;
import com.pzhu.acp.model.entity.Discuss;
import com.pzhu.acp.model.query.GetDiscussByPageQuery;
import com.pzhu.acp.model.vo.DiscussVO;

import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【discuss】的数据库操作Service
* @createDate 2022-11-21 21:34:27
*/
public interface DiscussService extends IService<Discuss> {

    boolean addDiscuss(Discuss discuss);

    boolean updateDiscuss(Discuss discuss);

    boolean deleteDiscuss(List<Long> ids);

    boolean upOrDownAction(Discuss discuss,String flag);

    Map<String, Object> getDiscussByPageOrParam(GetDiscussByPageQuery getDiscussByPageQuery);

    DiscussVO getDiscussInfoById(Long id);
}
