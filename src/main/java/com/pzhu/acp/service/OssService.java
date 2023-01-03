package com.pzhu.acp.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @Auther: gali
 * @Date: 2022-11-13 20:57
 * @Description:
 */
public interface OssService {
    /**
     * 上传文件
     * @param file
     * @return
     */
    String uploadFileAvatar(MultipartFile file);
}
