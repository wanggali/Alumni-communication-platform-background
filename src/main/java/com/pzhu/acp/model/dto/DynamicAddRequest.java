package com.pzhu.acp.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: gali
 * @Date: 2022-12-19 15:44
 * @Description:
 */
@Data
public class DynamicAddRequest implements Serializable {
    /**
     * 用户id
     */
    private Long uid;

    /**
     * 标签id
     */
    private Long tid;

    /**
     * 动态内容
     */
    private String content;
}
