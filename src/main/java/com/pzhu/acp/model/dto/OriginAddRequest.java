package com.pzhu.acp.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: gali
 * @Date: 2022-11-13 21:32
 * @Description:
 */
@Data
public class OriginAddRequest implements Serializable {
    /**
     * 所属学院
     */
    private Long cid;

    /**
     * 创建人
     */
    private Long uid;

    /**
     * 组织名称
     */
    private String name;

    /**
     * 组织头像
     */
    private String avatar;
}
