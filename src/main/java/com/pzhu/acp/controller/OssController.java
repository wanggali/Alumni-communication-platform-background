package com.pzhu.acp.controller;

import com.pzhu.acp.common.BaseResponse;
import com.pzhu.acp.common.ResultUtils;
import com.pzhu.acp.service.OssService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * @Auther: gali
 * @Date: 2022-11-13 20:58
 * @Description:
 */
@RestController
@RequestMapping("/oss")
public class OssController {
    @Resource
    private OssService ossService;

    @PostMapping("/upload")
    public BaseResponse<String> uploadOssFile(MultipartFile file) {
        String url = ossService.uploadFileAvatar(file);
        return ResultUtils.success(url);
    }
}
