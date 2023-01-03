package com.pzhu.acp.service;

import com.pzhu.acp.model.entity.Region;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pzhu.acp.model.dto.RegionAddRequest;
import com.pzhu.acp.model.dto.RegionUpdateRequest;
import com.pzhu.acp.model.vo.RegionTreeVO;

import java.util.List;

/**
* @author Administrator
* @description 针对表【region】的数据库操作Service
* @createDate 2022-10-01 20:50:00
*/
public interface RegionService extends IService<Region> {

    List<RegionTreeVO> getRegionTreeInfo();

    Boolean addRegion(RegionAddRequest regionAddRequest);

    Boolean deleteRegion(Long id,String levelId);

    Boolean updateRegion(RegionUpdateRequest regionUpdateRequest);
}
