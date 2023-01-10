package com.pzhu.acp.service;

import com.pzhu.acp.model.query.IndexInfoQuery;

import java.util.Map;

/**
 * @Auther: gali
 * @Date: 2023-01-09 21:35
 * @Description:
 */
public interface IndexService {
    Map<String, Object> getIndexInfo(IndexInfoQuery indexInfoQuery);
}
