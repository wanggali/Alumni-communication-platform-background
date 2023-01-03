package com.pzhu.acp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pzhu.acp.model.entity.Reply;

import java.util.List;

/**
* @author Administrator
* @description 针对表【reply】的数据库操作Service
* @createDate 2022-11-29 21:42:15
*/
public interface ReplyService extends IService<Reply> {

    boolean addReply(Reply reply);

    boolean updateUpNum(Reply reply);

    boolean deleteReply(List<Long> ids);
}
