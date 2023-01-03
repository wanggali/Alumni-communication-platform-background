package com.pzhu.acp.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pzhu.acp.model.entity.Tag;
import com.pzhu.acp.model.vo.TagVO;

import java.util.List;

/**
* @author Administrator
* @description 针对表【tag】的数据库操作Service
* @createDate 2022-12-05 23:15:59
*/
public interface TagService extends IService<Tag> {

    Boolean addTag(Tag tag);

    Boolean updateTag(Tag tag);

    Boolean deleteTag(List<Long> ids);

    List<TagVO> showAllTag();
}
