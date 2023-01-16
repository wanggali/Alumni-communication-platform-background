package com.pzhu.acp.model.query;

import com.pzhu.acp.enums.OrderTypeEnum;
import com.pzhu.acp.model.dto.WorkPageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-12-07 23:42
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GetDiscussByPageQuery extends WorkPageRequest implements Serializable {
    /**
     * 文章标题
     */
    private String title;

    /**
     * 排序规则（1 点赞最多排序，2 踩最多排序 不传为创建事件排序）
     */
    private Integer sortType;

    /**
     * 审核状态
     */
    private Integer isAuditType;

    /**
     * 标签id
     */
    private Long tid;

}
