package com.pzhu.acp.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-12-19 15:44
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DynamicDeleteRequest extends WorkDeleteRequest implements Serializable {

}
