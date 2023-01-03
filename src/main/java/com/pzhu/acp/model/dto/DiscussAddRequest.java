package com.pzhu.acp.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;

/**
 * @Auther: gali
 * @Date: 2022-11-28 22:51
 * @Description:
 */
@Data
@Setter
@Getter
public class DiscussAddRequest {
    /**
     * 用户id
     */
    private Long uid;

    /**
     * 标签id
     */
    private Long tid;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章内容
     */
    private String message;

    /**
     * 文章封面
     */
    private String cover;
}
