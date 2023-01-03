package com.pzhu.acp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.constant.OperationConstant;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.mapper.CollegeMapper;
import com.pzhu.acp.model.entity.College;
import com.pzhu.acp.model.query.GetCollegeByPageQuery;
import com.pzhu.acp.service.CollegeService;
import com.pzhu.acp.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @description 针对表【college】的数据库操作Service实现
 * @createDate 2022-12-05 21:01:14
 */
@Service
@Slf4j
public class CollegeServiceImpl extends ServiceImpl<CollegeMapper, College>
        implements CollegeService {

    @Resource
    private CollegeMapper collegeMapper;


    @Override
    public Boolean addCollege(College college) {
        checkCollegeExisted(college);
        int operationNum = collegeMapper.insert(college);
        if (operationNum == OperationConstant.OPERATION_NUM) {
            log.warn("新增学院失败,该参数为:{}", GsonUtil.toJson(college));
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }
        return Boolean.TRUE;
    }

    private void checkCollegeExisted(College college) {
        QueryWrapper<College> collegeQueryWrapper = new QueryWrapper<>();
        collegeQueryWrapper.eq("name", college.getName());
        Long count = collegeMapper.selectCount(collegeQueryWrapper);
        if (count > OperationConstant.COUNT_NUM) {
            log.warn("学院名字已存在，该参数为：{}", GsonUtil.toJson(college));
            throw new BusinessException(ErrorCode.EXISTED_NAME);
        }
    }

    @Override
    public Boolean updateCollege(College college) {
        QueryWrapper<College> collegeQueryWrapper = new QueryWrapper<>();
        collegeQueryWrapper.eq("id", college.getId());
        Long count = collegeMapper.selectCount(collegeQueryWrapper);
        if (count == OperationConstant.COUNT_NUM) {
            log.warn("该学院不存在，该参数为：{}", GsonUtil.toJson(college));
            throw new BusinessException(ErrorCode.NO_EXISTED_DATA);
        }
        checkCollegeExisted(college);
        int operationNum = collegeMapper.updateById(college);
        if (operationNum == OperationConstant.OPERATION_NUM) {
            log.warn("更新学院失败,该参数为:{}", GsonUtil.toJson(college));
            throw new BusinessException(ErrorCode.UPDATE_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean deleteCollege(List<Long> ids) {
        ids.forEach(id -> {
            QueryWrapper<College> collegeQueryWrapper = new QueryWrapper<>();
            collegeQueryWrapper.eq("id", id);
            Long count = collegeMapper.selectCount(collegeQueryWrapper);
            if (count == OperationConstant.COUNT_NUM) {
                log.warn("该学院不存在，该id为：{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.NO_EXISTED_DATA);
            }
            int operationNum = collegeMapper.deleteById(id);
            if (operationNum == OperationConstant.OPERATION_NUM) {
                log.warn("删除学院失败,该参数为:{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.DELETE_ERROR);
            }
        });
        return Boolean.TRUE;
    }

    @Override
    public Map<String, Object> ShowAllCollegeByPage(GetCollegeByPageQuery getCollegeByPageQuery) {
        Page<College> collegePage = new Page<>(getCollegeByPageQuery.getPageNum(), getCollegeByPageQuery.getPageSize());
        QueryWrapper<College> collegeQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isBlank(getCollegeByPageQuery.getName().trim())) {
            collegeQueryWrapper.like("name", getCollegeByPageQuery.getName());
        }
        collegeMapper.selectPage(collegePage, collegeQueryWrapper);
        Map<String, Object> map = Maps.newHashMap();
        map.put("items", collegePage.getRecords());
        map.put("current", collegePage.getCurrent());
        map.put("pages", collegePage.getPages());
        map.put("size", collegePage.getSize());
        map.put("total", collegePage.getTotal());
        return map;
    }
}




