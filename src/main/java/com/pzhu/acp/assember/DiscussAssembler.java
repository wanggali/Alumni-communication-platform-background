package com.pzhu.acp.assember;

import com.pzhu.acp.enums.OrderTypeEnum;
import com.pzhu.acp.model.dto.GetDiscussByPageRequest;
import com.pzhu.acp.model.query.GetDiscussByPageQuery;

/**
 * @Auther: gali
 * @Date: 2022-12-07 23:55
 * @Description:
 */
public class DiscussAssembler {
    /**
     * GetDiscussByPageRequest=>GetDiscussByPageQuery
     */
    public static GetDiscussByPageQuery toGetDiscussByPageQuery(GetDiscussByPageRequest getDiscussByPageRequest) {
        GetDiscussByPageQuery getDiscussByPageQuery = new GetDiscussByPageQuery();
        if (getDiscussByPageRequest.getTitle() != null) {
            getDiscussByPageQuery.setTitle(getDiscussByPageRequest.getTitle());
        }
        if (getDiscussByPageRequest.getSortType() != null) {
            getDiscussByPageQuery.setSortType(getDiscussByPageRequest.getSortType());
        }
        if (getDiscussByPageRequest.getIsAuditType() != null) {
            getDiscussByPageQuery.setIsAuditType(getDiscussByPageRequest.getIsAuditType());
        }
        getDiscussByPageQuery.setPageNum(getDiscussByPageRequest.getPageNum());
        getDiscussByPageQuery.setPageSize(getDiscussByPageRequest.getPageSize());
        return getDiscussByPageQuery;
    }
}
