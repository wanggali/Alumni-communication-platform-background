package com.pzhu.acp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.model.entity.User;
import com.pzhu.acp.model.entity.UserInfo;
import com.pzhu.acp.model.query.GetUserByPageQuery;
import com.pzhu.acp.model.query.UserUpdatePasswordQuery;
import com.pzhu.acp.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 用户服务
 *
 * @author yupi
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @return 新用户 id
     */
    Boolean userRegister(User user, String code);

    /**
     * 用户登录
     *
     * @param request
     * @return 脱敏后的用户信息
     */
    String userLogin(User user, HttpServletRequest request);

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);

    void getEmailCode(String email);

    UserVO getCurrentUser(String token);

    Boolean updateUser(User user, UserInfo userInfo);

    Map<String, Object> getUserByPage(GetUserByPageQuery getUserByPageQuery);

    Boolean deleteUser(List<Long> ids);

    String userAdminLogin(User user);

    Boolean updateUserPassword(UserUpdatePasswordQuery userUpdatePasswordQuery);
}
