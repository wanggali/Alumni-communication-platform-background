package com.pzhu.acp.model.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Auther: gali
 * @Date: 2022-12-21 13:40
 * @Description:
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GetNoticeByPageQuery extends WorkPageQuery implements Serializable {
}
