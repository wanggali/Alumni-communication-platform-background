package com.pzhu.acp.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName answer
 */
@TableName(value ="answer")
@Data
public class Answer implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 被回复人id
     */
    private Long uid;

    /**
     * 问题id
     */
    private Long qid;

    /**
     * 回复人id
     */
    private Long answerId;

    /**
     * 回复内容
     */
    private String answerContent;

    /**
     * 是否采纳（0未采纳，1采纳）
     */
    private Integer isAdopt;

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
        Answer other = (Answer) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUid() == null ? other.getUid() == null : this.getUid().equals(other.getUid()))
            && (this.getQid() == null ? other.getQid() == null : this.getQid().equals(other.getQid()))
            && (this.getAnswerId() == null ? other.getAnswerId() == null : this.getAnswerId().equals(other.getAnswerId()))
            && (this.getAnswerContent() == null ? other.getAnswerContent() == null : this.getAnswerContent().equals(other.getAnswerContent()))
            && (this.getIsAdopt() == null ? other.getIsAdopt() == null : this.getIsAdopt().equals(other.getIsAdopt()))
            && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUid() == null) ? 0 : getUid().hashCode());
        result = prime * result + ((getQid() == null) ? 0 : getQid().hashCode());
        result = prime * result + ((getAnswerId() == null) ? 0 : getAnswerId().hashCode());
        result = prime * result + ((getAnswerContent() == null) ? 0 : getAnswerContent().hashCode());
        result = prime * result + ((getIsAdopt() == null) ? 0 : getIsAdopt().hashCode());
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
        sb.append(", uid=").append(uid);
        sb.append(", qid=").append(qid);
        sb.append(", answerId=").append(answerId);
        sb.append(", answerContent=").append(answerContent);
        sb.append(", isAdopt=").append(isAdopt);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}