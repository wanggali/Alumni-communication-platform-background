package com.pzhu.acp.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.pzhu.acp.common.ErrorCode;
import com.pzhu.acp.constant.CommonConstant;
import com.pzhu.acp.constant.OperationConstant;
import com.pzhu.acp.constant.RedisConstant;
import com.pzhu.acp.email.MailProducer;
import com.pzhu.acp.enums.RoleEnum;
import com.pzhu.acp.enums.SexEnum;
import com.pzhu.acp.exception.BusinessException;
import com.pzhu.acp.mapper.*;
import com.pzhu.acp.model.entity.*;
import com.pzhu.acp.model.query.GetUserByPageQuery;
import com.pzhu.acp.model.query.UserUpdatePasswordQuery;
import com.pzhu.acp.model.vo.UserVO;
import com.pzhu.acp.service.UserService;
import com.pzhu.acp.utils.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 *
 * @author gali
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private MailProducer mailProducer;

    @Resource
    private CollegeMapper collegeMapper;

    @Resource
    private RegionMapper regionMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "acp_gali";

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, BusinessException.class})
    public Boolean userRegister(User user, String code) {
        QueryWrapper<College> collegeQueryWrapper = new QueryWrapper<>();
        collegeQueryWrapper.eq("id", user.getCollegeId());
        Long count = collegeMapper.selectCount(collegeQueryWrapper);
        if (count == OperationConstant.COUNT_NUM) {
            log.warn("该学院不存在，该用户参数为：{}", GsonUtil.toJson(user));
            throw new BusinessException(ErrorCode.NO_EXISTED_DATA);
        }
        QueryWrapper<Region> wrapper = new QueryWrapper<>();
        wrapper.eq("id", user.getRid());
        Long regionCount = regionMapper.selectCount(wrapper);
        if (regionCount == OperationConstant.COUNT_NUM) {
            log.warn("该地区不存在，该用户参数为：{}", GsonUtil.toJson(user));
            throw new BusinessException(ErrorCode.NO_EXISTED_DATA);
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("email", user.getEmail());
        Long userCount = userMapper.selectCount(userQueryWrapper);
        if (userCount > OperationConstant.COUNT_NUM) {
            log.warn("该邮箱已被注册，该用户参数为：{}", GsonUtil.toJson(user));
            throw new BusinessException(ErrorCode.EXISTED_EMAIL);
        }
        //2.判断缓存中是否有验证码
        String key = String.format(RedisConstant.EMAIL_CODE, user.getEmail());
        String redisCode = GsonUtil.fromJson((String) redisTemplate.opsForValue().get(key), String.class);
        if (StringUtils.isBlank(redisCode)) {
            log.warn("验证码已过期，请重新获取验证码");
            throw new BusinessException(ErrorCode.EXPIRE_EMAIL_CODE);
        }
        if (!code.equals(redisCode)) {
            log.warn("验证码错误，注册失败，该code参数为：{}", GsonUtil.toJson(code));
            throw new BusinessException(ErrorCode.ERROR_EMAIL_CODE);
        }
        // 3. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + user.getPasssword()).getBytes());
        user.setPasssword(encryptPassword);
        String s = RandomUtil.randomString(6);
        user.setUsername("校友" + s);
        user.setAvatar(CommonConstant.DEFAULT_AVATAR);
        int operationNum = userMapper.insert(user);
        if (operationNum == OperationConstant.OPERATION_NUM) {
            log.warn("新增用户失败,该用户参数为:{}", GsonUtil.toJson(user));
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setId(user.getId());
        userInfo.setEmail(user.getEmail());
        int userInfoOperationNum = userInfoMapper.insert(userInfo);
        if (userInfoOperationNum == OperationConstant.OPERATION_NUM) {
            log.warn("新增用户详细失败,该用户参数为:{}", GsonUtil.toJson(userInfo));
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }

        //4.初始化用户角色 默认校友
        UserRole userRole = new UserRole();
        userRole.setRoleId(RoleEnum.STUDENT.getRoleId());
        userRole.setUserId(user.getId());

        int userRoleOperationNum = userRoleMapper.insert(userRole);
        if (userRoleOperationNum == OperationConstant.OPERATION_NUM) {
            log.warn("新增用户角色失败,该用户角色参数为:{}", GsonUtil.toJson(userRole));
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }

        //5.删除redis验证码缓存
        redisTemplate.delete(key);

        return Boolean.TRUE;
    }

    @Override
    public String userLogin(User user, HttpServletRequest request) {
        // 1. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + user.getPasssword()).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", user.getEmail());
        queryWrapper.eq("passsword", encryptPassword);
        User dataUser = userMapper.selectOne(queryWrapper);
        // 用户不存在
        if (dataUser == null) {
            log.warn("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.NO_EXISTED_USER);
        }
        // 2. 用户脱敏
        User safetyUser = getSafetyUser(dataUser);
        // 3. 记录用户的登录态
        StpUtil.login(safetyUser.getId());
        return StpUtil.getTokenInfo().tokenValue;
    }

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setRid(originUser.getRid());
        safetyUser.setCollegeId(originUser.getCollegeId());
        StrUtil.hide(originUser.getEmail(), 3, 3);
        safetyUser.setAdminType(originUser.getAdminType());
        safetyUser.setPasssword(DesensitizedUtil.password(originUser.getPasssword()));
        safetyUser.setEmail(StrUtil.hide(originUser.getEmail(), 3, 3));
        safetyUser.setAvatar(originUser.getAvatar());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setSign(originUser.getSign());
        safetyUser.setStatus(originUser.getStatus());
        safetyUser.setIsDelete(originUser.getIsDelete());
        safetyUser.setCreateTime(new Date(originUser.getCreateTime().getTime()));
        safetyUser.setUpdateTime(new Date(originUser.getUpdateTime().getTime()));
        return safetyUser;
    }

    /**
     * 用户注销
     *
     * @param request
     */
    @Override
    public int userLogout(HttpServletRequest request) {
        // 移除登录态
        StpUtil.logout();
        return 1;
    }

    @Override
    public void getEmailCode(String email) {
        String key = String.format(RedisConstant.EMAIL_CODE, email);
        String redisCode = GsonUtil.fromJson((String) redisTemplate.opsForValue().get(key), String.class);
        if (StringUtils.isNotBlank(redisCode)) {
            log.warn("该redis中还存在验证码，验证码未过期");
            throw new BusinessException(ErrorCode.NO_EXPIRE_EMAIL_CODE);
        }
        mailProducer.sendEmail(email);
    }

    @Override
    public UserVO getCurrentUser(String token) {
        if (StringUtils.isBlank((String) StpUtil.getLoginIdByToken(token))) {
            log.warn("token已过期，请重新登录");
            throw new BusinessException(ErrorCode.TIMEOUT_TOKEN);
        }
        Long id = Long.parseLong((String) StpUtil.getLoginIdByToken(token));
        UserVO user = userMapper.selectUserById(id);
        user.setJoinTime(new Date(user.getJoinTime().getTime()));
        user.getUserInfo().setJoinTime(new Date(user.getUserInfo().getJoinTime().getTime()));
        user.getUserInfo().setSexType(Objects.nonNull(user.getUserInfo().getSex()) ?
                SexEnum.getOrderTypeEnum(user.getUserInfo().getSex()).getMsg() : "未知");
        return user;
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, BusinessException.class})
    public Boolean updateUser(User user, UserInfo userInfo) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", user.getId());
        Long count = userMapper.selectCount(userQueryWrapper);
        if (count == OperationConstant.COUNT_NUM) {
            log.warn("该用户不存在，该参数为：{}", GsonUtil.toJson(user));
            throw new BusinessException(ErrorCode.NO_EXISTED_USER);
        }
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("id", user.getId());
        Long userInfoCount = userInfoMapper.selectCount(userInfoQueryWrapper);
        if (userInfoCount == OperationConstant.COUNT_NUM) {
            log.warn("该用户不存在，该参数为：{}", GsonUtil.toJson(userInfo));
            throw new BusinessException(ErrorCode.NO_EXISTED_USER);
        }
        int operationNum = userMapper.updateById(user);
        if (operationNum == OperationConstant.OPERATION_NUM) {
            log.warn("更新用户基本信息失败,该用户参数为:{}", GsonUtil.toJson(user));
            throw new BusinessException(ErrorCode.UPDATE_ERROR);
        }
        int userInfoOperationNum = userInfoMapper.updateById(userInfo);
        if (userInfoOperationNum == OperationConstant.OPERATION_NUM) {
            log.warn("更新用户详细信息失败,该用户参数为:{}", GsonUtil.toJson(userInfo));
            throw new BusinessException(ErrorCode.UPDATE_ERROR);
        }
        return Boolean.TRUE;
    }

    @Override
    public Map<String, Object> getUserByPage(GetUserByPageQuery getUserByPageQuery) {
        Page<UserVO> page = new Page<>(getUserByPageQuery.getPageNum(), getUserByPageQuery.getPageSize());
        IPage<UserVO> result = userMapper.selectUserByPage(page, getUserByPageQuery);
        List<UserVO> records = result.getRecords();
        if (StringUtils.isNotBlank(getUserByPageQuery.getUserName())) {
            records = records.stream().filter(item -> item.getUserName().contains(getUserByPageQuery.getUserName())).collect(Collectors.toList());
        }
        if (getUserByPageQuery.getCollegeId() != null) {
            records = records.stream().filter(item -> item.getCollegeId().equals(getUserByPageQuery.getCollegeId())).collect(Collectors.toList());
        }
        if (getUserByPageQuery.getRegionId() != null) {
            records = records.stream().filter(item -> item.getRegionId().equals(getUserByPageQuery.getRegionId())).collect(Collectors.toList());
        }
        records.forEach(item -> {
            item.setJoinTime(new Date(item.getJoinTime().getTime()));
            item.getUserInfo().setJoinTime(new Date(item.getUserInfo().getJoinTime().getTime()));
            item.getUserInfo().setBirthday(Objects.nonNull(item.getUserInfo().getBirthday())
                    ? new Date(item.getUserInfo().getBirthday().getTime()) : null);
        });
        Map<String, Object> map = Maps.newHashMap();
        map.put("items", records);
        map.put("current", page.getCurrent());
        map.put("pages", page.getPages());
        map.put("size", page.getSize());
        map.put("total", page.getTotal());
        return map;
    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, BusinessException.class})
    public Boolean deleteUser(List<Long> ids) {
        ids.forEach(id -> {
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("id", id);
            Long count = userMapper.selectCount(userQueryWrapper);
            if (count == OperationConstant.COUNT_NUM) {
                log.warn("该用户不存在，该参数为：{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.NO_EXISTED_USER);
            }
            QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
            userInfoQueryWrapper.eq("id", id);
            Long userInfoCount = userInfoMapper.selectCount(userInfoQueryWrapper);
            if (userInfoCount == OperationConstant.COUNT_NUM) {
                log.warn("该用户不存在，该参数为：{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.NO_EXISTED_USER);
            }
            int operationNum = userMapper.deleteById(id);
            if (operationNum == OperationConstant.OPERATION_NUM) {
                log.warn("删除用户基本信息失败,该用户参数为:{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.DELETE_ERROR);
            }
            int userInfoOperationNum = userInfoMapper.deleteById(id);
            if (userInfoOperationNum == OperationConstant.OPERATION_NUM) {
                log.warn("删除用户详细信息失败,该用户参数为:{}", GsonUtil.toJson(id));
                throw new BusinessException(ErrorCode.DELETE_ERROR);
            }
        });
        return Boolean.TRUE;
    }

    @Override
    public String userAdminLogin(User user) {
        //验证邮箱正确性
        String validPattern = "\\\\w[-\\\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\\\.)+[A-Za-z]{2,14}";
        Matcher matcher = Pattern.compile(validPattern).matcher(user.getEmail());
        if (matcher.find()) {
            log.warn("邮箱格式不正确，该邮箱为：{}", user.getEmail());
            throw new BusinessException(ErrorCode.ERROR_EMAIL);
        }
        // 1. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + user.getPasssword()).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", user.getEmail());
        queryWrapper.eq("passsword", encryptPassword);
        User dataUser = userMapper.selectOne(queryWrapper);
        // 用户不存在
        if (dataUser == null) {
            log.warn("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.NO_EXISTED_USER);

        }

        // 2. 用户脱敏
        User safetyUser = getSafetyUser(dataUser);
        if (dataUser.getAdminType() == 0) {
            log.warn("该用户不是管理员，用户信息为：{}", GsonUtil.toJson(safetyUser));
            throw new BusinessException(ErrorCode.NO_AUTH);
        }

        // 3. 记录用户的登录态
        StpUtil.login(safetyUser.getId());
        return StpUtil.getTokenInfo().tokenValue;
    }

    @Override
    public Boolean updateUserPassword(UserUpdatePasswordQuery userUpdatePasswordQuery) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("id", userUpdatePasswordQuery.getId());
        User user = userMapper.selectOne(userQueryWrapper);
        if (user == null) {
            log.warn("该用户不存在，用户id为：{}", userUpdatePasswordQuery.getId());
            throw new BusinessException(ErrorCode.NO_EXISTED_USER);
        }
        String oldPassword = DigestUtils.md5DigestAsHex((SALT + userUpdatePasswordQuery.getOldPassword()).getBytes());
        if (!oldPassword.equals(user.getPasssword())) {
            log.warn("该用户旧密码错误，用户id为：{}", userUpdatePasswordQuery.getId());
            throw new BusinessException(ErrorCode.ERROR_OLD_PASSWORD);
        }
        String newPassword = DigestUtils.md5DigestAsHex((SALT + userUpdatePasswordQuery.getNewPassword()).getBytes());
        user.setPasssword(newPassword);
        int operationNum = userMapper.updateById(user);
        if (operationNum == OperationConstant.OPERATION_NUM) {
            log.warn("更新用户密码失败,该用户参数为:{}", GsonUtil.toJson(user));
            throw new BusinessException(ErrorCode.UPDATE_ERROR);
        }
        return Boolean.TRUE;
    }
}




