package com.pzhu.acp.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-12-31 0:22
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RoleDeleteRequest extends WorkDeleteRequest implements Serializable {

}
