package com.pzhu.acp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.constant.OperationConstant;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.model.entity.Notice;
import com.pzhu.acp.model.query.GetNoticeByPageQuery;
import com.pzhu.acp.service.NoticeService;
import com.pzhu.acp.mapper.NoticeMapper;
import com.pzhu.acp.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @description 针对表【notice】的数据库操作Service实现
 * @createDate 2022-12-20 17:55:38
 */
@Service
@Slf4j
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice>
        implements NoticeService {

    @Resource
    private NoticeMapper noticeMapper;

    @Override
    public Boolean addNotice(Notice notice) {
        int operationNum = noticeMapper.insert(notice);
        if (operationNum == OperationConstant.OPERATION_NUM) {
            log.warn("添加公告失败,该问题参数为:{}", GsonUtil.toJson(notice));
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean updateNotice(Notice notice) {
        checkNoticeExisted(notice);
        int operationNum = noticeMapper.updateById(notice);
        if (operationNum == OperationConstant.OPERATION_NUM) {
            log.warn("更新公告失败,该问题参数为:{}", GsonUtil.toJson(notice));
            throw new BusinessException(ErrorCode.UPDATE_ERROR);
        }
        return Boolean.TRUE;
    }

    private void checkNoticeExisted(Notice notice) {
        QueryWrapper<Notice> noticeQueryWrapper = new QueryWrapper<>();
        noticeQueryWrapper.eq("id", notice.getId());
        Long count = noticeMapper.selectCount(noticeQueryWrapper);
        if (count == OperationConstant.COUNT_NUM) {
            log.warn("不存在该条公告，该参数为：{}", GsonUtil.toJson(notice));
            throw new BusinessException(ErrorCode.NO_EXISTED_DATA);
        }
    }

    @Override
    public Boolean deleteNotice(List<Long> ids) {
        ids.forEach(id -> {
            Notice notice = new Notice();
            notice.setId(id);
            checkNoticeExisted(notice);
            int operationNum = noticeMapper.deleteById(id);
            if (operationNum == OperationConstant.OPERATION_NUM) {
                log.warn("删除公告失败,该问题参数为:{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.DELETE_ERROR);
            }
        });
        return Boolean.TRUE;
    }

    @Override
    public Map<String, Object> getNoticeByPage(GetNoticeByPageQuery getNoticeByPageQuery) {
        Page<Notice> page = new Page<>(getNoticeByPageQuery.getPageNum(), getNoticeByPageQuery.getPageSize());
        QueryWrapper<Notice> noticeQueryWrapper = new QueryWrapper<>();
        noticeQueryWrapper.orderByDesc("create_time");
        noticeMapper.selectPage(page, noticeQueryWrapper);
        List<Notice> records = page.getRecords();
        records.forEach(item -> item.setCreateTime(new Date(item.getCreateTime().getTime())));
        Map<String, Object> map = Maps.newHashMap();
        map.put("items", records);
        map.put("current", page.getCurrent());
        map.put("pages", page.getPages());
        map.put("size", page.getSize());
        map.put("total", page.getTotal());
        return map;
    }
}




