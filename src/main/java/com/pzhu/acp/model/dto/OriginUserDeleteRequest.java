package com.pzhu.acp.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Auther: gali
 * @Date: 2022-10-03 22:17
 * @Description:
 */
@Data
public class OriginUserDeleteRequest implements Serializable {

    /**
     * 组织用户id
     */
    private List<Long> ids;

    /**
     * 组织id
     */
    private Long oid;

    /**
     * 用户id
     */
    private List<Long> uids;

    private static final long serialVersionUID = 604678205224377007L;
}
