package com.pzhu.acp.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-12-21 13:50
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BannerDeleteRequest extends WorkDeleteRequest implements Serializable {

}
