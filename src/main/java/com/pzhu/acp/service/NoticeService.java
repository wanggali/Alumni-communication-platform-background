package com.pzhu.acp.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.pzhu.acp.model.entity.Notice;
import com.pzhu.acp.model.query.GetNoticeByPageQuery;

import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【notice】的数据库操作Service
* @createDate 2022-12-20 17:55:38
*/
public interface NoticeService extends IService<Notice> {

    Boolean addNotice(Notice notice);

    Boolean updateNotice(Notice notice);

    Boolean deleteNotice(List<Long> ids);

    Map<String, Object> getNoticeByPage(GetNoticeByPageQuery getNoticeByPageQuery);
}
