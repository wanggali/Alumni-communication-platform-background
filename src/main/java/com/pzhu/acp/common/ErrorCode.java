package com.pzhu.acp.common;

/**
 * 错误码
 *
 * @author gali
 */
public enum ErrorCode {

    SUCCESS(0, "ok", ""),
    TRY_REDISSON_LOCK_ERROR(30000, "redisson尝试加锁失败", ""),
    PARAMS_ERROR(40000, "请求参数错误", ""),
    NULL_ERROR(40001, "请求数据为空", ""),
    NOT_LOGIN(40100, "未登录", ""),
    NO_AUTH(40101, "无权限", ""),
    SYSTEM_ERROR(50000, "系统内部异常", ""),
    SAVE_ERROR(50010, "新增失败", ""),
    UPDATE_ERROR(50020, "更新失败", ""),
    DELETE_ERROR(50030, "删除失败", ""),
    EXISTED_NAME(50040, "名字已存在", ""),
    NO_EXISTED_DATA(50050, "不存在该条数据", ""),
    EXISTED_USER_ORIGIN(50060, "该用户已加入该组织", ""),
    EXISTED_DISCUSS(50070, "该条讨论不存在", ""),
    MORE_ORIGIN_PEOPLE_NUM(50080, "超过组织人数的最大值", ""),
    REDIS_DATA_IS_NULL(50090, "缓存中数据为null", ""),
    NO_EXISTED_USER(50100, "该用户不存在", ""),
    ERROR_ENUM_PARAM(50200, "错误的枚举类型", ""),
    NO_EXISTED_TAG(50300, "该标签不存在", ""),
    ANSWER_QUESTION_EXISTED(50400, "该用户已回答了该问题", ""),
    EXISTED_PUSH_INFO(50500, "已存在相同的内推信息", ""),
    ERROR_EMAIL_CODE(50600, "错误的邮件验证码", ""),
    NO_EXPIRE_EMAIL_CODE(50700, "邮箱验证码未过期", ""),
    EXPIRE_EMAIL_CODE(50800, "邮箱验证码已过期", ""),
    EXISTED_EMAIL(50900, "该邮箱已被注册", ""),
    TIMEOUT_TOKEN(51000, "登录已过期，请重新登录", ""),
    ERROR_EMAIL(51100, "错误的邮箱格式", ""),
    ERROR_OLD_PASSWORD(51200, "用户旧密码错误", ""),
    SAME_PASSWORD(51300, "用户新旧密码相同", ""),
    EXISTED_ROLE_NAME(51400, "已存在该角色", ""),
    EXISTED_PERMISSION_PATH(51500, "菜单路径已重复", ""),
    EXISTED_PERMISSION_CHILDREN(51600, "菜单下有子菜单", ""),
    USER_ROLE_IN_SAME(51700, "用户角色为当前设定值", "");

    private final int code;

    /**
     * 状态码信息
     */
    private final String message;

    /**
     * 状态码描述（详情）
     */
    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
