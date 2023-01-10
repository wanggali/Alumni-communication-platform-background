package com.pzhu.acp.controller.front;

import com.pzhu.acp.common.BaseResponse;
import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.common.ResultUtils;
import com.pzhu.acp.constant.CommonConstant;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.model.dto.IndexInfoRequest;
import com.pzhu.acp.model.query.IndexInfoQuery;
import com.pzhu.acp.service.IndexService;
import com.pzhu.acp.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Auther: gali
 * @Date: 2023-01-09 21:34
 * @Description:
 */
@RestController
@Slf4j
@RequestMapping("/front/index")
public class IndexController {

    @Resource
    private IndexService indexService;

    /**
     * 分页获取首页 帖子、问题、动态信息
     * 默认三条
     */
    @PostMapping("/getIndexInfo")
    public BaseResponse<Map<String, Object>> getIndexInfo(@RequestBody IndexInfoRequest indexInfoRequest) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        IndexInfoQuery indexInfoQuery = new IndexInfoQuery();
        BeanUtils.copyProperties(indexInfoRequest, indexInfoQuery);
        CompletableFuture<Map<String, Object>> future = CompletableFuture.supplyAsync(()
                -> indexService.getIndexInfo(indexInfoQuery), executorService);
        return ResultUtils.success(future.get());
    }
}
