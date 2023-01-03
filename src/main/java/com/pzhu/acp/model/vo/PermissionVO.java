package com.pzhu.acp.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.pzhu.acp.model.entity.Permission;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Auther: gali
 * @Date: 2022-12-31 0:59
 * @Description:
 */
@Data
public class PermissionVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 所属上级
     */
    private Long pid;

    /**
     * 名称
     */
    private String label;

    /**
     * 类型（1菜单2按钮）
     */
    private Integer type;

    /**
     * 权限值
     */
    private String permissionValue;

    /**
     * 访问路径
     */
    private String path;

    /**
     * 状态（1禁用）
     */
    private Integer state;

    /**
     * 子菜单
     */
    private List<PermissionVO> children;
}
