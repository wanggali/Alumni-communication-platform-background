package com.pzhu.acp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pzhu.acp.model.entity.Banner;
import com.pzhu.acp.model.query.GetBannerByPageQuery;

import java.util.List;
import java.util.Map;

/**
* @author Administrator
* @description 针对表【banner】的数据库操作Service
* @createDate 2022-12-20 17:55:18
*/
public interface BannerService extends IService<Banner> {

    Boolean addBanner(Banner banner);

    Boolean updateBanner(Banner banner);

    Boolean deleteBanner(List<Long> ids);

    Map<String, Object> getBannerByPage(GetBannerByPageQuery getBannerByPageQuery);
}
