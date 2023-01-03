package com.pzhu.acp.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-12-05 21:19
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetRoleByPageRequest extends WorkPageRequest implements Serializable {

}
