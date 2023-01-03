package com.pzhu.acp.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-12-21 13:51
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetBannerByPageRequest extends WorkPageRequest implements Serializable {
}
