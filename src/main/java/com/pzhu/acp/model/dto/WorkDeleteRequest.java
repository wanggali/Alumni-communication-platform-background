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
public class WorkDeleteRequest implements Serializable {

    /**
     * id
     */
    private List<Long> ids;


    private static final long serialVersionUID = 604678205224377007L;
}
