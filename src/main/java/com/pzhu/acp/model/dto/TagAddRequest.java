package com.pzhu.acp.model.dto;

import lombok.Data;
import org.springframework.boot.convert.DataSizeUnit;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-12-05 23:20
 * @Description:
 */
@Data
public class TagAddRequest implements Serializable {
    /**
     * 标签名字
     */
    private String name;
}
