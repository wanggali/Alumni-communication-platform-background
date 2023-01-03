package com.pzhu.acp.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-12-19 15:44
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetPushByPageRequest extends WorkPageRequest implements Serializable {
    /**
     * 用户id
     */
    private Long uid;

    /**
     * 公司名字
     */
    private String companyName;

    /**
     * 公司职位
     */
    private String companyPosition;

    /**
     * 公司地点
     */
    private String companyRegion;

    /**
     * 薪资
     */
    private String companySalary;

    /**
     * 是否审核通过（0审核中，1通过，2未通过）
     */
    private Integer isAudit;

    /**
     * 排序规则
     */
    private Integer sortType;
}
