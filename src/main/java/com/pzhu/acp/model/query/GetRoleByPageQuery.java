package com.pzhu.acp.model.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-12-21 13:52
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetRoleByPageQuery extends WorkPageQuery implements Serializable {
}
