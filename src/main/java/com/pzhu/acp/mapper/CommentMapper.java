package com.pzhu.acp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pzhu.acp.model.entity.Comment;
import com.pzhu.acp.model.query.GetCommentQuery;
import com.pzhu.acp.model.vo.CommentVO;
import org.apache.ibatis.annotations.Param;

/**
 * @author Administrator
 * @description 针对表【comment】的数据库操作Mapper
 * @createDate 2022-11-29 21:42:10
 * @Entity com.pzhu.acp.model.Comment
 */
public interface CommentMapper extends BaseMapper<Comment> {

    IPage<CommentVO> selectCommentByPageOrParam(Page<CommentVO> commentVOPage, @Param("query") GetCommentQuery getCommentQuery);
}




