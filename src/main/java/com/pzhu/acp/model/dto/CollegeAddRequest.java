package com.pzhu.acp.model.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: gali
 * @Date: 2022-12-05 21:19
 * @Description:
 */
@Data
public class CollegeAddRequest implements Serializable {
    /**
     * 学院名称
     */
    private String name;

}
