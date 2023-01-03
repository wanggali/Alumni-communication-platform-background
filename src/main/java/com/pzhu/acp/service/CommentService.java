package com.pzhu.acp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pzhu.acp.model.entity.Comment;
import com.pzhu.acp.model.query.GetCommentQuery;
import com.pzhu.acp.model.vo.CommentVO;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @description 针对表【comment】的数据库操作Service
 * @createDate 2022-11-29 21:42:10
 */
public interface CommentService extends IService<Comment> {

    boolean addComment(Comment comment);

    boolean updateComment(Comment comment);

    boolean deleteComment(List<Long> ids);

    Map<String,Object> getCommentByPageOrParam(GetCommentQuery getCommentQuery);
}
