package com.pzhu.acp.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Auther: gali
 * @Date: 2022-11-28 22:51
 * @Description:
 */
@Data
@Setter
@Getter
public class DiscussDeleteRequest {
    /**
     * 用户ids
     */
    private List<Long> ids;
}
