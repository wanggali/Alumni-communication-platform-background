package com.pzhu.acp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.constant.OperationConstant;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.mapper.BannerMapper;
import com.pzhu.acp.model.entity.Banner;
import com.pzhu.acp.model.query.GetBannerByPageQuery;
import com.pzhu.acp.service.BannerService;
import com.pzhu.acp.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @description 针对表【banner】的数据库操作Service实现
 * @createDate 2022-12-20 17:55:18
 */
@Service
@Slf4j
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner>
        implements BannerService {

    @Resource
    private BannerMapper bannerMapper;

    @Override
    public Boolean addBanner(Banner banner) {
        QueryWrapper<Banner> bannerQueryWrapper = new QueryWrapper<>();
        bannerQueryWrapper.eq("name", banner.getName());
        Long count = bannerMapper.selectCount(bannerQueryWrapper);
        if (count > OperationConstant.COUNT_NUM) {
            log.warn("名字已重复，该参数为：{}", GsonUtil.toJson(banner));
            throw new BusinessException(ErrorCode.EXISTED_NAME);
        }
        int operationNum = bannerMapper.insert(banner);
        if (operationNum == OperationConstant.OPERATION_NUM) {
            log.warn("新增轮播图失败,该问题参数为:{}", GsonUtil.toJson(banner));
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean updateBanner(Banner banner) {
        checkBannerExisted(banner);
        int operationNum = bannerMapper.updateById(banner);
        if (operationNum == OperationConstant.OPERATION_NUM) {
            log.warn("更新轮播图失败,该问题参数为:{}", GsonUtil.toJson(banner));
            throw new BusinessException(ErrorCode.UPDATE_ERROR);
        }
        return Boolean.TRUE;
    }

    private void checkBannerExisted(Banner banner) {
        QueryWrapper<Banner> bannerQueryWrapper = new QueryWrapper<>();
        bannerQueryWrapper.eq("id", banner.getId());
        Long count = bannerMapper.selectCount(bannerQueryWrapper);
        if (count == OperationConstant.COUNT_NUM) {
            log.warn("该轮播图不存在，该参数为：{}", GsonUtil.toJson(banner));
            throw new BusinessException(ErrorCode.NO_EXISTED_DATA);
        }
    }

    @Override
    public Boolean deleteBanner(List<Long> ids) {
        ids.forEach(id -> {
            Banner banner = new Banner();
            banner.setId(id);
            checkBannerExisted(banner);
            int operationNum = bannerMapper.deleteById(id);
            if (operationNum == OperationConstant.OPERATION_NUM) {
                log.warn("删除轮播图失败,该问题参数为:{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.DELETE_ERROR);
            }
        });
        return Boolean.TRUE;
    }

    @Override
    public Map<String, Object> getBannerByPage(GetBannerByPageQuery getBannerByPageQuery) {
        Page<Banner> page = new Page<>(getBannerByPageQuery.getPageNum(), getBannerByPageQuery.getPageSize());
        bannerMapper.selectPage(page, null);
        List<Banner> records = page.getRecords();
        records.forEach(item -> {
            item.setCreateTime(new Date(item.getCreateTime().getTime()));
        });
        Map<String, Object> map = Maps.newHashMap();
        map.put("items", records);
        map.put("current", page.getCurrent());
        map.put("pages", page.getPages());
        map.put("size", page.getSize());
        map.put("total", page.getTotal());
        return map;
    }
}




