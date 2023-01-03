package com.pzhu.acp.model.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Auther: gali
 * @Date: 2022-12-20 20:57
 * @Description:
 */
@Data
public class PushVO implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 公司职位
     */
    private String companyPosition;

    /**
     * 公司地点
     */
    private String companyRegion;

    /**
     * 薪资
     */
    private String companySalary;

    /**
     * 职位描述
     */
    private String positionInfo;

    /**
     * 职位数量
     */
    private Integer positionNum;

    /**
     * 内推链接
     */
    private String pushUrl;

    /**
     * 内推人
     */
    private Long uid;

    /**
     * 内推人信息
     */
    private UserVO userInfo;

    /**
     * 是否审核通过（0审核中，1通过，2未通过）
     */
    private Integer isAudit;

    /**
     * 创建时间
     */
    private Date createTime;

}
