package com.pzhu.acp.assember;

import com.pzhu.acp.model.entity.Origin;
import com.pzhu.acp.model.vo.OriginVO;
import org.joda.time.DateTime;

/**
 * @Auther: gali
 * @Date: 2022-11-14 21:49
 * @Description:
 */
public class OriginAssembler {
    /**
     * 类型转换：origin-》originVO
     */
    public static OriginVO toOriginVO(Origin origin, String regionName, String userName) {
        OriginVO originVO = new OriginVO();
        originVO.setId(origin.getId());
        originVO.setCollegeName(regionName);
        originVO.setCid(origin.getCid());
        originVO.setUid(origin.getUid());
        originVO.setUserName(userName);
        originVO.setOriginName(origin.getName());
        originVO.setAvatar(origin.getAvatar());
        originVO.setIsDelete(origin.getIsDelete());
        String createTime = new DateTime(origin.getCreateTime()).toString("yyyy/MM/dd");
        originVO.setCreateTime(createTime);
        return originVO;
    }
}
