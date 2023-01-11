package com.pzhu.acp.model.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-11-13 21:56
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetOriginQuery extends WorkPageQuery implements Serializable {
    /**
     * 学院
     */
    private Long collegeId;

    /**
     * 用户
     */
    private String userName;

    /**
     * 组织名
     */
    private String originName;
}
