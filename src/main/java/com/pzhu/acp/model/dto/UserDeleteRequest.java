package com.pzhu.acp.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户登录请求体
 *
 * @author gali
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserDeleteRequest extends WorkDeleteRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;
}
