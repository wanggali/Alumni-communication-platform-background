package com.pzhu.acp.model.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2023-01-09 21:41
 * @Description:
 */
@Data
public class IndexInfoQuery implements Serializable {
    /**
     * 标签id
     */
    private Long tid;
}
