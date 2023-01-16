package com.pzhu.acp.model.query;

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
public class GetQuestionByPageQuery extends WorkPageRequest implements Serializable {
    /**
     * 是否审核通过（0审核中，1通过，2未通过）
     */
    private Integer isAudit;

    /**
     * 标题
     */
    private String title;

    /**
     * 标签id
     */
    private Long tid;

}
