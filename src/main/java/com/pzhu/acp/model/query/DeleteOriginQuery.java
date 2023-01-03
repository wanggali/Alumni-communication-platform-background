package com.pzhu.acp.model.query;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: gali
 * @Date: 2022-11-25 23:27
 * @Description:
 */
@Data
public class DeleteOriginQuery implements Serializable {
    /**
     * 组织ids
     */
    List<Long> ids;
}
