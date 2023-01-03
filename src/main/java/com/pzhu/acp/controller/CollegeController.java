package com.pzhu.acp.controller;

import com.pzhu.acp.common.BaseResponse;
import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.common.ResultUtils;
import com.pzhu.acp.constant.CommonConstant;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.model.dto.CollegeAddRequest;
import com.pzhu.acp.model.dto.CollegeDeleteRequest;
import com.pzhu.acp.model.dto.CollegeUpdateRequest;
import com.pzhu.acp.model.dto.GetCollegeByPageRequest;
import com.pzhu.acp.model.entity.College;
import com.pzhu.acp.model.query.GetCollegeByPageQuery;
import com.pzhu.acp.service.CollegeService;
import com.pzhu.acp.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Auther: gali
 * @Date: 2022-12-05 21:06
 * @Description:
 */
@RestController
@Slf4j
@RequestMapping("/college")
public class CollegeController {
    @Resource
    private CollegeService collegeService;

    /**
     * 添加学院
     */
    @PostMapping("/addCollege")
    public BaseResponse<Boolean> addCollege(@RequestBody CollegeAddRequest collegeAddRequest) {
        if (StringUtils.isBlank(collegeAddRequest.getName().trim())) {
            log.warn("学院名字为空");
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        College college = new College();
        college.setName(collegeAddRequest.getName());
        Boolean isSuccess = collegeService.addCollege(college);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 更新学院
     */
    @PostMapping("/updateCollege")
    public BaseResponse<Boolean> updateCollege(@RequestBody CollegeUpdateRequest collegeUpdateRequest) {
        if (collegeUpdateRequest.getId() < CommonConstant.MIN_ID) {
            log.warn("学院id为不合法,该学院id为:{}", GsonUtil.toJson(collegeUpdateRequest.getId()));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (StringUtils.isBlank(collegeUpdateRequest.getName().trim())) {
            log.warn("学院名字为空");
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        College college = new College();
        college.setId(collegeUpdateRequest.getId());
        college.setName(collegeUpdateRequest.getName());
        Boolean isSuccess = collegeService.updateCollege(college);
        return ResultUtils.success(isSuccess);
    }

    /**
     * 删除学院
     */
    @DeleteMapping("/deleteCollege")
    public BaseResponse<Boolean> deleteCollege(@RequestBody CollegeDeleteRequest collegeDeleteRequest) {
        collegeDeleteRequest.getIds().forEach(id -> {
            if (id < CommonConstant.MIN_ID) {
                log.warn("学院id为不合法,该学院id为:{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        });
        Boolean isSuccess = collegeService.deleteCollege(collegeDeleteRequest.getIds());
        return ResultUtils.success(isSuccess);
    }

    /**
     * 分页展示学院
     */
    @PostMapping("/ShowAllCollegeByPage")
    public BaseResponse<Map<String, Object>> ShowAllCollegeByPage(@RequestBody GetCollegeByPageRequest getCollegeByPageRequest) {
        if (getCollegeByPageRequest.getPageNum() < CommonConstant.MIN_PAGE_NUM ||
                getCollegeByPageRequest.getPageSize() < CommonConstant.MIN_PAGE_SIZE) {
            log.warn("分页参数出错了，该参数为：{}", GsonUtil.toJson(getCollegeByPageRequest));
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        GetCollegeByPageQuery getCollegeByPageQuery = new GetCollegeByPageQuery();
        getCollegeByPageQuery.setPageNum(getCollegeByPageRequest.getPageNum());
        getCollegeByPageQuery.setPageSize(getCollegeByPageRequest.getPageSize());
        getCollegeByPageQuery.setName(getCollegeByPageRequest.getName());
        Map<String, Object> map = collegeService.ShowAllCollegeByPage(getCollegeByPageQuery);
        return ResultUtils.success(map);
    }
}
