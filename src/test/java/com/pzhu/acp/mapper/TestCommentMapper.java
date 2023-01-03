package com.pzhu.acp.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pzhu.acp.model.query.GetCommentQuery;
import com.pzhu.acp.model.vo.CommentVO;
import com.pzhu.acp.model.vo.OriginUserVO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @Auther: gali
 * @Date: 2022-11-22 22:18
 * @Description:
 */
@SpringBootTest
public class TestCommentMapper {
    @Resource
    private CommentMapper commentMapper;

    @Test
    public void testSelectComment(){
        GetCommentQuery getCommentQuery = new GetCommentQuery();
        getCommentQuery.setDid(1L);
        getCommentQuery.setPageNum(1);
        getCommentQuery.setPageSize(5);
        Page<CommentVO> commentVOPage = new Page<>(getCommentQuery.getPageNum(), getCommentQuery.getPageSize());
        System.out.println(commentMapper.selectCommentByPageOrParam(commentVOPage, getCommentQuery).getRecords());
    }
}
