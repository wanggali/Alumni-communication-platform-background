package com.pzhu.acp.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-12-20 20:33
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PushDeleteRequest extends WorkDeleteRequest implements Serializable {
}
