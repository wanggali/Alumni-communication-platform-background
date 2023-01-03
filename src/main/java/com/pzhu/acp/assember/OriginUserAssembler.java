package com.pzhu.acp.assember;

import com.pzhu.acp.model.dto.OriginUserDeleteRequest;
import com.pzhu.acp.model.entity.Origin;
import com.pzhu.acp.model.query.DeleteOriginUserQuery;
import com.pzhu.acp.model.vo.OriginVO;
import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;

/**
 * @Auther: gali
 * @Date: 2022-11-14 21:49
 * @Description:
 */
public class OriginUserAssembler {
    /**
     * 类型转换：OriginUserDeleteRequest-》DeleteOriginUserQuery
     */
    public static DeleteOriginUserQuery toDeleteOriginUserQuery(OriginUserDeleteRequest originUserDeleteRequest) {
        DeleteOriginUserQuery deleteOriginUserQuery = new DeleteOriginUserQuery();
        deleteOriginUserQuery.setId(originUserDeleteRequest.getIds());
        deleteOriginUserQuery.setOid(originUserDeleteRequest.getOid());
        deleteOriginUserQuery.setUids(originUserDeleteRequest.getUids());
        return deleteOriginUserQuery;
    }
}
