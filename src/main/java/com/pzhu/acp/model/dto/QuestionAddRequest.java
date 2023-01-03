package com.pzhu.acp.model.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

/**
 * @Auther: gali
 * @Date: 2022-12-16 15:11
 * @Description:
 */
@Data
public class QuestionAddRequest {
    /**
     * 用户id
     */
    private Long uid;

    /**
     * 问题标题
     */
    private String title;

    /**
     * 问题内容
     */
    private String content;

    /**
     * 标签id
     */
    private Long tid;
}
