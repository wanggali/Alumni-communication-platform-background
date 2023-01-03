package com.pzhu.acp.model.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-12-05 21:40
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GetCollegeByPageQuery extends WorkPageQuery implements Serializable {
    /**
     * 学院名字
     */
    private String name;
}
