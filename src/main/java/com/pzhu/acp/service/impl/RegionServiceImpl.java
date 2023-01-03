package com.pzhu.acp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.constant.OperationConstant;
import com.pzhu.acp.constant.RedisConstant;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.mapper.RegionMapper;
import com.pzhu.acp.model.dto.RegionAddRequest;
import com.pzhu.acp.model.dto.RegionUpdateRequest;
import com.pzhu.acp.model.entity.Region;
import com.pzhu.acp.model.vo.RegionTreeVO;
import com.pzhu.acp.service.RegionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Administrator
 * @description 针对表【region】的数据库操作Service实现
 * @createDate 2022-10-01 20:50:00
 */
@Service
@Slf4j
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region>
        implements RegionService {

    @Resource
    private RegionMapper regionMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public List<RegionTreeVO> getRegionTreeInfo() {
        List<RegionTreeVO> redisList = (List<RegionTreeVO>) redisTemplate.opsForList().leftPop(RedisConstant.REGION_NAME);
        if (!CollectionUtils.isEmpty(redisList)) {
            log.info("redis缓存存在数据，直接返回");
            return redisList;
        } else {
            List<Region> allRegionList = regionMapper.selectList(null);
            //递归查询地区，并返回封装类RegionTreeVo
            List<RegionTreeVO> list = buildRegionTree(allRegionList);
            //添加至redis中的list缓存中
            redisTemplate.opsForList().leftPush(RedisConstant.REGION_NAME, list);
            return list;
        }
    }

    @Override
    public Boolean addRegion(RegionAddRequest regionAddRequest) {
        Region region = new Region();
        BeanUtils.copyProperties(regionAddRequest, region);
        //根据地区编码查询是否存在该信息
        QueryWrapper<Region> wrapper = new QueryWrapper<>();
        wrapper.eq("code", region.getCode());
        Long count = regionMapper.selectCount(wrapper);
        if (count > OperationConstant.COUNT_NUM) {
            log.warn("地区编码已经存在");
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }
        //判断父类是否存在
        QueryWrapper<Region> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("parent_id", region.getParentId());
        Long countParent = regionMapper.selectCount(wrapper1);
        if (countParent == OperationConstant.COUNT_NUM) {
            log.warn("父类地区不存在");
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }
        return regionMapper.insert(region) > OperationConstant.OPERATION_NUM;
    }

    @Override
    public Boolean deleteRegion(Long id, String levelId) {
        //判断当前id是否存在数据库
        QueryWrapper<Region> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        if (levelId.equals("3")) {
            Long count = regionMapper.selectCount(wrapper);
            if (count == OperationConstant.COUNT_NUM) {
                log.warn("该id地区不存在：{}", id);
                throw new BusinessException(ErrorCode.DELETE_ERROR);
            }
        } else {
            List<RegionTreeVO> regionTreeInfo = this.getRegionTreeInfo();
            for (RegionTreeVO regionTreeVo : regionTreeInfo) {
                if (Objects.equals(regionTreeVo.getId(), id) && regionTreeVo.getChildren().size() > 0) {
                    log.warn("该地区含有子类，不能删除：{}", id);
                    throw new BusinessException(ErrorCode.DELETE_ERROR);
                }
            }
        }
        return regionMapper.deleteById(id) > OperationConstant.OPERATION_NUM;
    }

    @Override
    public Boolean updateRegion(RegionUpdateRequest regionUpdateRequest) {
        Region region = new Region();
        BeanUtils.copyProperties(regionUpdateRequest, region);
        //判断当前id是否存在数据库
        QueryWrapper<Region> wrapper = new QueryWrapper<>();
        wrapper.eq("id", region.getId());
        Long count = regionMapper.selectCount(wrapper);
        if (count < 0) {
            log.warn("该id地区不存在：{}", region.getId());
            throw new BusinessException(ErrorCode.UPDATE_ERROR);
        }
        return regionMapper.updateById(region) > OperationConstant.OPERATION_NUM;
    }

    private List<RegionTreeVO> buildRegionTree(List<Region> allRegionList) {
        //创建最终封装对象类
        List<RegionTreeVO> regionTree = new ArrayList<>();
        QueryWrapper<Region> wrapper = new QueryWrapper<>();
        wrapper.ne("level_id", 0);
        List<Region> list = regionMapper.selectList(wrapper);
        for (Region region : allRegionList) {
            if ("0".equals(region.getLevelId())) {
                RegionTreeVO regionTreeVo = new RegionTreeVO();
                BeanUtils.copyProperties(region, regionTreeVo);
                regionTree.add(selectRegionTreeList(regionTreeVo, list));
            }
        }
        return regionTree;
    }

    private RegionTreeVO selectRegionTreeList(RegionTreeVO regionTreeVo, List<Region> allRegionList) {
        //对象初始化
        regionTreeVo.setChildren(new ArrayList<>());
        for (Region region : allRegionList) {
            if (region.getParentId().equals(regionTreeVo.getId())) {
                RegionTreeVO regionTreeVoChildren = new RegionTreeVO();
                BeanUtils.copyProperties(region, regionTreeVoChildren);
                //如果children为空，进行初始化操作
                if (regionTreeVo.getParentId() == null) {
                    regionTreeVo.setChildren(new ArrayList<>());
                }
                regionTreeVo.getChildren().add(selectRegionTreeList(regionTreeVoChildren, allRegionList));
            }
        }
        return regionTreeVo;
    }
}




