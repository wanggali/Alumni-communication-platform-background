package com.pzhu.acp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.constant.OperationConstant;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.model.entity.Tag;
import com.pzhu.acp.model.vo.TagVO;
import com.pzhu.acp.service.TagService;
import com.pzhu.acp.mapper.TagMapper;
import com.pzhu.acp.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @description 针对表【tag】的数据库操作Service实现
 * @createDate 2022-12-05 23:15:59
 */
@Service
@Slf4j
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag>
        implements TagService {

    @Resource
    private TagMapper tagMapper;

    @Override
    public Boolean addTag(Tag tag) {
        checkTagNameExisted(tag);
        int operationNum = tagMapper.insert(tag);
        if (operationNum == OperationConstant.OPERATION_NUM) {
            log.warn("添加标签失败,该标签名为:{}", GsonUtil.toJson(tag.getName()));
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }
        return Boolean.TRUE;
    }

    private void checkTagNameExisted(Tag tag) {
        QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
        tagQueryWrapper.eq("name", tag.getName());
        Long count = tagMapper.selectCount(tagQueryWrapper);
        if (count > OperationConstant.COUNT_NUM) {
            log.warn("标签名字重复，该标签名为：{}", GsonUtil.toJson(tag.getName()));
            throw new BusinessException(ErrorCode.EXISTED_NAME);
        }
    }

    @Override
    public Boolean updateTag(Tag tag) {
        //检查该标签是否存在
        checkTagExisted(tag);
        //检查该标签名是否重复
        checkTagNameExisted(tag);
        int operationNum = tagMapper.updateById(tag);
        if (operationNum == OperationConstant.OPERATION_NUM) {
            log.warn("更新标签失败,该标签名为:{}", GsonUtil.toJson(tag.getName()));
            throw new BusinessException(ErrorCode.UPDATE_ERROR);
        }
        return Boolean.TRUE;
    }

    private void checkTagExisted(Tag tag) {
        QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
        tagQueryWrapper.eq("id", tag.getId());
        Long count = tagMapper.selectCount(tagQueryWrapper);
        if (count == OperationConstant.COUNT_NUM) {
            log.warn("标签不存在，该标签为：{}", GsonUtil.toJson(tag));
            throw new BusinessException(ErrorCode.NO_EXISTED_DATA);
        }
    }

    @Override
    public Boolean deleteTag(List<Long> ids) {
        ids.forEach(id -> {
            Tag tag = new Tag();
            tag.setId(id);
            checkTagExisted(tag);
            int operationNum = tagMapper.deleteById(id);
            if (operationNum == OperationConstant.OPERATION_NUM) {
                log.warn("删除标签失败,该标签id为:{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.DELETE_ERROR);
            }
        });
        return Boolean.TRUE;
    }

    @Override
    public List<TagVO> showAllTag() {
        return tagMapper.selectList(null)
                .stream()
                .map(item -> {
                    TagVO tagVO = new TagVO();
                    tagVO.setId(item.getId());
                    tagVO.setName(item.getName());
                    return tagVO;
                }).collect(Collectors.toList());
    }
}




