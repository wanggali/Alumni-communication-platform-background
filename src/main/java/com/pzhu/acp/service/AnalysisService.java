package com.pzhu.acp.service;

import java.util.Map;

/**
 * @Auther: gali
 * @Date: 2022-12-28 23:54
 * @Description:
 */
public interface AnalysisService {
    Map<String, Object> analysisInTotal();

    Map<String, Object> analysisInWeek();

    Map<String, Object> analysisInComment();

    Map<String, Object> analysisInUp();
}
