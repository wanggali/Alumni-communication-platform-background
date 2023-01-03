package com.pzhu.acp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pzhu.acp.model.entity.College;
import com.pzhu.acp.model.query.GetCollegeByPageQuery;

import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【college】的数据库操作Service
* @createDate 2022-12-05 21:01:14
*/
public interface CollegeService extends IService<College> {

    Boolean addCollege(College college);

    Boolean updateCollege(College college);

    Boolean deleteCollege(List<Long> ids);

    Map<String, Object> ShowAllCollegeByPage(GetCollegeByPageQuery getCollegeByPageQuery);
}
