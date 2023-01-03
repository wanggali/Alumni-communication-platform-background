package com.pzhu.acp.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @TableName push
 */
@TableName(value = "push")
@Data
public class Push implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
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
     * 是否审核通过（0审核中，1通过，2未通过）
     */
    private Integer isAudit;

    /**
     * 逻辑删除（0未删除1删除）
     */
    private Integer isDelete;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Push other = (Push) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
                && (this.getCompanyName() == null ? other.getCompanyName() == null : this.getCompanyName().equals(other.getCompanyName()))
                && (this.getCompanyPosition() == null ? other.getCompanyPosition() == null : this.getCompanyPosition().equals(other.getCompanyPosition()))
                && (this.getCompanyRegion() == null ? other.getCompanyRegion() == null : this.getCompanyRegion().equals(other.getCompanyRegion()))
                && (this.getCompanySalary() == null ? other.getCompanySalary() == null : this.getCompanySalary().equals(other.getCompanySalary()))
                && (this.getPositionInfo() == null ? other.getPositionInfo() == null : this.getPositionInfo().equals(other.getPositionInfo()))
                && (this.getPositionNum() == null ? other.getPositionNum() == null : this.getPositionNum().equals(other.getPositionNum()))
                && (this.getPushUrl() == null ? other.getPushUrl() == null : this.getPushUrl().equals(other.getPushUrl()))
                && (this.getUid() == null ? other.getUid() == null : this.getUid().equals(other.getUid()))
                && (this.getIsAudit() == null ? other.getIsAudit() == null : this.getIsAudit().equals(other.getIsAudit()))
                && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getCompanyName() == null) ? 0 : getCompanyName().hashCode());
        result = prime * result + ((getCompanyPosition() == null) ? 0 : getCompanyPosition().hashCode());
        result = prime * result + ((getCompanyRegion() == null) ? 0 : getCompanyRegion().hashCode());
        result = prime * result + ((getCompanySalary() == null) ? 0 : getCompanySalary().hashCode());
        result = prime * result + ((getPositionInfo() == null) ? 0 : getPositionInfo().hashCode());
        result = prime * result + ((getPositionNum() == null) ? 0 : getPositionNum().hashCode());
        result = prime * result + ((getPushUrl() == null) ? 0 : getPushUrl().hashCode());
        result = prime * result + ((getUid() == null) ? 0 : getUid().hashCode());
        result = prime * result + ((getIsAudit() == null) ? 0 : getIsAudit().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", companyName=").append(companyName);
        sb.append(", companyPosition=").append(companyPosition);
        sb.append(", companyRegion=").append(companyRegion);
        sb.append(", companySalary=").append(companySalary);
        sb.append(", positionInfo=").append(positionInfo);
        sb.append(", positionNum=").append(positionNum);
        sb.append(", pushUrl=").append(pushUrl);
        sb.append(", uid=").append(uid);
        sb.append(", isAudit=").append(isAudit);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}