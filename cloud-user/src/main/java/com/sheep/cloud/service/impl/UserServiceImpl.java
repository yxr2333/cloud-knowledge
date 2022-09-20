package com.sheep.cloud.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.crypto.SecureUtil;
import com.sheep.cloud.dao.*;
import com.sheep.cloud.entity.*;
import com.sheep.cloud.request.*;
import com.sheep.cloud.response.ApiResult;
import com.sheep.cloud.response.IUsersBaseInfoDTO;
import com.sheep.cloud.response.PageData;
import com.sheep.cloud.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.service.impl
 * @datetime 2022/8/12 星期五
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private IUsersEntityRepository usersEntityRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ILabelsEntityRepository labelsEntityRepository;

    @Autowired
    private IScoreListEntityRepository scoreListEntityRepository;

    @Autowired
    private ICollectListsEntityRepository collectListsEntityRepository;

    @Autowired
    private IResourcesEntityRepository resourcesEntityRepository;

    @Autowired
    private IWishesEntityRepository wishesEntityRepository;

    /**
     * 用户登录
     *
     * @param dto 用户登录信息
     * @return 登录结果
     */
    @Override
    public ApiResult doLogin(IUsersLoginVO dto) {
        IUsersEntity entity = usersEntityRepository
                .findIUsersEntityByUsername(dto.getUsername()).orElseThrow(() -> new RuntimeException("用户不存在"));
        String salt = entity.getSalt();
        String password = SecureUtil.md5(dto.getPassword() + salt);
        log.info("password:{}", password);
        log.info("entity.getPassword():{}", entity.getPassword());
        if (password.equals(entity.getPassword())) {
            StpUtil.login(entity.getUid() + entity.getUsername());
            Map<String, Object> result = MapUtil.builder(new HashMap<String, Object>(16))
                    .put("token", StpUtil.getTokenValue())
                    .put("tokenName", StpUtil.getTokenName())
                    .put("userInfo", entity)
                    .build();
            return ApiResult.success("登录成功", result);
        } else {
            return ApiResult.error("用户名或密码错误");
        }
    }

    /**
     * 用户注册
     *
     * @param dto 用户注册信息
     * @return 注册结果
     */
    @Override
    public ApiResult doRegister(IUsersRegisterVO dto) {
        if (usersEntityRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }
        String salt = userRegister(dto);
        // 将dto转换为entity
        IUsersEntity entity = new IUsersEntity();
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());
        entity.setSalt(salt);
        entity.setEmail(dto.getEmail());
        entity.setDescription(dto.getDescription() == null ? "" : dto.getDescription());
        usersEntityRepository.save(entity);
        return ApiResult.success("注册成功");
    }

    private String userRegister(IUsersRegisterVO vo) {
        String randomString = RandomUtil.randomString(10);
        String salt = Base64.encode(randomString);
        String password = SecureUtil.md5(vo.getPassword() + salt);
        vo.setPassword(password);
        return salt;
    }

    /**
     * 远程调用用户注册
     *
     * @param registerVO 用户注册信息
     * @return 注册结果
     */
    @Override
    public ApiResult remoteMakeUserRegister(IUsersRegisterVO registerVO) {
        String salt = userRegister(registerVO);
        return ApiResult.success(salt, registerVO);
    }

    /**
     * 用户重置密码
     *
     * @param request         请求对象
     * @param resetPasswordVO 用户重置密码信息
     * @return 重置密码结果
     */
    public ApiResult resetPassword(HttpServletRequest request, IUsersResetPasswordVO resetPasswordVO) {
        Object resetCode = request.getSession().getAttribute("reset_code");
        log.info("resetCode:" + resetCode);
        if (resetCode == null) {
            return ApiResult.error("未发送验证码或验证码已过期");
        }
        if (!resetCode.toString().equals(resetPasswordVO.getCode())) {
            return ApiResult.error("验证码错误");
        }
        String randomString = RandomUtil.randomString(10);
        String salt = Base64.encode(randomString);
        String password = SecureUtil.md5(resetPasswordVO.getNewPassword() + salt);
        IUsersEntity entity = usersEntityRepository.findIUsersEntityByUsername(resetPasswordVO.getUsername()).orElseThrow(() -> new RuntimeException("用户不存在"));
        // 重新设置密码
        entity.setSalt(salt);
        entity.setPassword(password);

        // 删除验证码
        request.removeAttribute("reset_code");

        usersEntityRepository.save(entity);

        return ApiResult.success("重置密码成功");
    }

    /**
     * 用户修改信息
     *
     * @param modifyInfoVO 用户修改信息
     * @return 修改结果
     */
    @Override
    public ApiResult modifyInfo(IUsersModifyInfoVO modifyInfoVO) {
        IUsersEntity entity = usersEntityRepository
                .findById(modifyInfoVO.getId())
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        // 判断邮箱是否合法
        if (StringUtils.hasText(modifyInfoVO.getEmail())) {
            boolean match = ReUtil.isMatch("^(([^<>()[\\]\\\\.,;:\\s@\"]+(\\.[^<>()[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))", modifyInfoVO.getEmail());
            if (!match) {
                return ApiResult.error("邮箱格式错误");
            } else {
                entity.setEmail(modifyInfoVO.getEmail());
            }
        }
        if (StringUtils.hasText(modifyInfoVO.getAvatar())) {
            entity.setAvatar(modifyInfoVO.getAvatar());
        }
        if (StringUtils.hasText(modifyInfoVO.getUsername()) && !entity.getUsername().equals(modifyInfoVO.getUsername())) {
            if (usersEntityRepository.existsByUsername(modifyInfoVO.getUsername())) {
                return ApiResult.error("用户名已存在");
            } else {
                entity.setUsername(modifyInfoVO.getUsername());
            }
        }
        if (StringUtils.hasText(modifyInfoVO.getDescription())) {
            entity.setDescription(modifyInfoVO.getDescription());
        }
        if (!Objects.isNull(modifyInfoVO.getLabels())) {
            entity.setLabels(modifyInfoVO.getLabels());
        }
        usersEntityRepository.save(entity);
        return ApiResult.success("修改更新成功");
    }

    /**
     * 通过用户名id删除用户
     *
     * @param id 用户id
     * @return 删除结果
     */
    @Override
    public ApiResult deleteUserById(Integer id) {
        // 判断是否存在并删除
        if (usersEntityRepository.existsById(id)) {
            usersEntityRepository.deleteById(id);
            return ApiResult.success("删除成功");
        } else {
            return ApiResult.error("用户不存在");
        }
    }

    /**
     * 通过用户名id查询用户
     *
     * @param id 用户id
     * @return 查询结果
     */
    @Override
    public ApiResult getOne(Integer id) {
        IUsersEntity entity = usersEntityRepository.findById(id).orElseThrow(() -> new RuntimeException("用户不存在"));
        // 数据脱敏后返回

        IUsersBaseInfoDTO result = modelMapper.map(entity, IUsersBaseInfoDTO.class);
        return ApiResult.success(result);
    }

    /**
     * 通过用户名模糊查询用户
     *
     * @param name     用户名
     * @param pageNum  页码
     * @param pageSize 页大小
     * @return 查询结果
     */
    public ApiResult getAllLikeName(String name, Integer pageNum, Integer pageSize) {
        PageRequest pageable = PageRequest.of(pageNum, pageSize);
        Page<IUsersEntity> page = usersEntityRepository.findAllByUsernameLike(name, pageable);
        return getApiResult(page);
    }

    /**
     * 获取所有用户
     *
     * @param pageNum  页码
     * @param pageSize 页大小
     * @return 查询结果
     */
    @Override
    public ApiResult getAll(Integer pageNum, Integer pageSize) {
        PageRequest pageable = PageRequest.of(pageNum, pageSize);
        Page<IUsersEntity> page = usersEntityRepository.findAll(pageable);
        return getApiResult(page);
    }

    private ApiResult getApiResult(Page<IUsersEntity> page) {
        List<IUsersBaseInfoDTO> result = page.getContent().stream()
                .map(e -> modelMapper.map(e, IUsersBaseInfoDTO.class))
                .collect(Collectors.toList());
        PageData.PageDataBuilder<IUsersBaseInfoDTO> builder = PageData.builder();
        return ApiResult.success(builder.totalPage(page.getTotalPages())
                .totalNum(page.getTotalElements())
                .data(result)
                .build());
    }

    /**
     * 给用户添加积分
     *
     * @param vo 添加积分信息
     * @return 添加结果
     */
    @Override
    public ApiResult addScore(IUsersAddScoreVO vo) {
        IUsersEntity entity = usersEntityRepository.findById(vo.getId()).orElseThrow(() -> new RuntimeException("用户不存在"));
        synchronized (this) {
            entity.setScore(entity.getScore() + vo.getScore());
        }
        return ApiResult.success("添加积分成功");
    }


    /**
     * 查询收藏记录
     *
     * @param uid 用户id
     * @return 查询结果
     */
    @Override
    public ApiResult findCollectList(Integer uid) {
        List<ICollectListsEntity> queryResult = collectListsEntityRepository.findAllByUserUid(uid);
        if (CollectionUtils.isEmpty(queryResult)) {
            return ApiResult.warning("暂无收藏记录");
        } else {
            return ApiResult.success(queryResult);
        }
    }

    /**
     * 查询发布记录
     *
     * @param uid 用户id
     * @return 查询结果
     */
    @Override
    public ApiResult findPublishList(Integer uid) {
        List<IResourcesEntity> queryResult = resourcesEntityRepository.findAllByPublishUserUid(uid);
        if (CollectionUtils.isEmpty(queryResult)) {
            return ApiResult.warning("暂无发表记录");
        } else {
            return ApiResult.success(queryResult);
        }
    }

    /**
     * 查询心愿墙发表记录
     *
     * @param uid 用户id
     * @return 查询结果
     */
    @Override
    public ApiResult findWishList(Integer uid) {
        List<IWishesEntity> queryResult = wishesEntityRepository.findAllByUserUid(uid);
        if (CollectionUtils.isEmpty(queryResult)) {
            return ApiResult.warning("暂无心愿墙记录");
        } else {
            return ApiResult.success(queryResult);
        }
    }

    /**
     * 查询积分记录
     *
     * @param uid 用户id
     * @return 查询结果
     */
    @Override
    public ApiResult findScoreList(Integer uid) {
        List<IScoreListEntity> queryResult = scoreListEntityRepository.findAllByUserUid(uid);
        if (CollectionUtils.isEmpty(queryResult)) {
            return ApiResult.warning("暂无积分记录");
        } else {
            return ApiResult.success(queryResult);
        }
    }

    /**
     * 根据标签查询用户
     *
     * @param labelId  标签id
     * @param pageNum  页码
     * @param pageSize 页大小
     * @return 查询结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult findAllByLabelId(List<Integer> labelId, Integer pageNum, Integer pageSize) {
        ArrayList<ILabelsEntity> labels = new ArrayList<>();
        // 根据标签编号查询标签
        labelId.forEach(id -> {
            ILabelsEntity labelsEntity = labelsEntityRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("标签不存在"));
            labels.add(labelsEntity);
        });

        PageRequest pageable = PageRequest.of(pageNum, pageSize);

        // 查出符合条件的用户
        Page<IUsersEntity> page = usersEntityRepository.findDistinctAllByLabelsIn(labels, pageable);
        return getApiResult(page);
//        return ApiResult.success(page.getContent());
    }


    @Override
    public ApiResult findAllByNameAndLabelId(String name, List<Integer> labelId, Integer pageNum, Integer pageSize) {
        List<ILabelsEntity> labels = labelsEntityRepository.findAllById(labelId);
        Sort sort = Sort.by(Sort.Direction.ASC, "uid");
        PageRequest pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<IUsersEntity> page = null;
        if (!StringUtils.hasText(name)) {
            page = usersEntityRepository.findAllByLabelsIn(labels, pageable);
        } else if (CollectionUtils.isEmpty(labelId)) {
            name = "%" + name + "%";
            page = usersEntityRepository.findAllByUsernameLike(name, pageable);
        } else {
            name = "%" + name + "%";
            page = usersEntityRepository.findAllByUsernameLikeAndLabelsIn(name, labels, pageable);
        }
        return getApiResult(page);
    }
}
