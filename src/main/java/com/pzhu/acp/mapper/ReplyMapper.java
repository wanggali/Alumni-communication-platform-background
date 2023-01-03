package com.pzhu.acp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pzhu.acp.model.entity.Reply;
import com.pzhu.acp.model.vo.ReplyVO;
import org.apache.ibatis.annotations.Param;

/**
 * @author Administrator
 * @description 针对表【reply】的数据库操作Mapper
 * @createDate 2022-11-29 21:42:15
 * @Entity com.pzhu.acp.model.Reply
 */
public interface ReplyMapper extends BaseMapper<Reply> {
    ReplyVO selectReplyVOByUserIdAndCommentId(@Param("uid") Long uid, @Param("cid") Long cid);
}




