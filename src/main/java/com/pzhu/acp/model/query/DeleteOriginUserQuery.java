package com.pzhu.acp.model.query;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: gali
 * @Date: 2022-11-25 23:03
 * @Description:
 */
@Data
public class DeleteOriginUserQuery implements Serializable {
    /**
     * 组织用户id
     */
    private List<Long> id;

    /**
     * 组织id
     */
    private Long oid;

    /**
     * 用户id
     */
    private List<Long> uids;
}
