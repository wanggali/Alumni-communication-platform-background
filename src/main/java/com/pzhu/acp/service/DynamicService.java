package com.pzhu.acp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pzhu.acp.model.entity.Dynamic;
import com.pzhu.acp.model.query.GetDynamicByPageQuery;
import com.pzhu.acp.model.vo.DynamicVO;

import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【dynamic】的数据库操作Service
* @createDate 2022-12-19 15:38:27
*/
public interface DynamicService extends IService<Dynamic> {

    Boolean addDynamic(Dynamic dynamic);

    Boolean updateByUp(Dynamic dynamic);

    Boolean updateDynamic(Dynamic dynamic);

    Boolean deleteDynamic(List<Long> ids);

    Map<String, Object> getDynamicByPageOrParam(GetDynamicByPageQuery getDynamicByPageQuery);

    DynamicVO getDynamicById(Long id);
}
