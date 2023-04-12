package com.pzhu.acp.constant;

/**
 * @Auther: gali
 * @Date: 2022-11-30 23:31
 * @Description:
 */
public interface RedisConstant {

    /**
     * redis缓存地区名字
     */
    String REGION_NAME = "Region_Name";

    /**
     * redis缓存验证码
     */
    String EMAIL_CODE = "email_code_%s";

    /**
     * 回复点赞 redis-key
     */
    String REPLY_BASE_UP_KEY = "reply_up_key";

    /**
     * 评论点赞 redis-key
     */
    String COMMENT_BASE_UP_KEY = "comment_up_key";

    /**
     * 帖子点赞 redis-key
     */
    String DISCUSS_BASE_UP_KEY = "discuss_up_key";

    /**
     * 动态点赞 redis-key
     */
    String DYNAMIC_BASE_UP_KEY = "dynamic_up_key";

    /**
     * 帖子踩 redis-key
     */
    String DISCUSS_DOWN_KEY = "discuss_DOWN_key";

    String DISCUSS_UP_USER_IDS = "discuss_up_user_ids";
    String DYNAMIC_UP_USER_IDS = "dynamic_up_user_ids";
    String COMMENT_UP_USER_IDS = "comment_up_user_ids";
    String REPLY_UP_USER_IDS = "reply_up_user_ids";
    String DISCUSS_DOWN_USER_IDS = "discuss_down_user_ids";
}
