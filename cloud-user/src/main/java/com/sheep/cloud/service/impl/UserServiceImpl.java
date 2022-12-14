package com.sheep.cloud.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.crypto.SecureUtil;
import com.sheep.cloud.dao.knowledge.*;
import com.sheep.cloud.dto.request.knowledge.*;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.dto.response.PageData;
import com.sheep.cloud.dto.response.knowledge.IUserLoginResDTO;
import com.sheep.cloud.dto.response.knowledge.IUsersBaseInfoDTO;
import com.sheep.cloud.entity.knowledge.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.service.impl
 * @datetime 2022/8/12 ζζδΊ
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
     * η¨ζ·η»ε½
     *
     * @param dto η¨ζ·η»ε½δΏ‘ζ―
     * @return η»ε½η»ζ
     */
    @Override
    public ApiResult<?> doLogin(IUsersLoginVO dto) {
        IUsersEntity entity = usersEntityRepository
                .findIUsersEntityByUsername(dto.getUsername()).orElseThrow(() -> new RuntimeException("η¨ζ·δΈε­ε¨"));
        String salt = entity.getSalt();
        String password = SecureUtil.md5(dto.getPassword() + salt);
        log.info("password:{}", password);
        log.info("entity.getPassword():{}", entity.getPassword());
        if (password.equals(entity.getPassword())) {
            StpUtil.login(entity.getUid() + entity.getUsername());
            IUserLoginResDTO resDTO = new IUserLoginResDTO(StpUtil.getTokenValue(), StpUtil.getTokenName(), entity);
            return new ApiResult<IUserLoginResDTO>().success("η»ε½ζε", resDTO);
        } else {
            return new ApiResult<>().error("η¨ζ·εζε―η ιθ――");
        }
    }

    /**
     * η¨ζ·ζ³¨ε
     *
     * @param dto η¨ζ·ζ³¨εδΏ‘ζ―
     * @return ζ³¨εη»ζ
     */
    @Override
    public ApiResult<?> doRegister(IUsersRegisterVO dto) {
        if (usersEntityRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("η¨ζ·εε·²ε­ε¨");
        }
        String salt = userRegister(dto);
        // ε°dtoθ½¬ζ’δΈΊentity
        IUsersEntity entity = new IUsersEntity();
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());
        entity.setSalt(salt);
        entity.setEmail(dto.getEmail());
        entity.setDescription(dto.getDescription() == null ? "" : dto.getDescription());
        usersEntityRepository.save(entity);
        return new ApiResult<>().success("ζ³¨εζε");
    }

    private String userRegister(IUsersRegisterVO vo) {
        String randomString = RandomUtil.randomString(10);
        String salt = Base64.encode(randomString);
        String password = SecureUtil.md5(vo.getPassword() + salt);
        vo.setPassword(password);
        return salt;
    }

    /**
     * θΏη¨θ°η¨η¨ζ·ζ³¨ε
     *
     * @param registerVO η¨ζ·ζ³¨εδΏ‘ζ―
     * @return ζ³¨εη»ζ
     */
    @Override
    public ApiResult<?> remoteMakeUserRegister(IUsersRegisterVO registerVO) {
        String salt = userRegister(registerVO);
        return new ApiResult<IUsersRegisterVO>().success(salt, registerVO);
    }

    /**
     * η¨ζ·ιη½?ε―η 
     *
     * @param request         θ―·ζ±ε―Ήθ±‘
     * @param resetPasswordVO η¨ζ·ιη½?ε―η δΏ‘ζ―
     * @return ιη½?ε―η η»ζ
     */
    @Override
    public ApiResult<?> resetPassword(HttpServletRequest request, IUsersResetPasswordVO resetPasswordVO) {
        Object resetCode = request.getSession().getAttribute("reset_code");
        log.info("resetCode:" + resetCode);
        if (resetCode == null) {
            return new ApiResult<>().error("ζͺειιͺθ―η ζιͺθ―η ε·²θΏζ");
        }
        if (!resetCode.toString().equals(resetPasswordVO.getCode())) {
            return new ApiResult<>().error("ιͺθ―η ιθ――");
        }
        String randomString = RandomUtil.randomString(10);
        String salt = Base64.encode(randomString);
        String password = SecureUtil.md5(resetPasswordVO.getNewPassword() + salt);
        IUsersEntity entity = usersEntityRepository.findIUsersEntityByUsername(resetPasswordVO.getUsername()).orElseThrow(() -> new RuntimeException("η¨ζ·δΈε­ε¨"));
        // ιζ°θ?Ύη½?ε―η 
        entity.setSalt(salt);
        entity.setPassword(password);

        // ε ι€ιͺθ―η 
        request.removeAttribute("reset_code");

        usersEntityRepository.save(entity);

        return new ApiResult<>().success("ιη½?ε―η ζε");
    }

    /**
     * η¨ζ·δΏ?ζΉδΏ‘ζ―
     *
     * @param modifyInfoVO η¨ζ·δΏ?ζΉδΏ‘ζ―
     * @return δΏ?ζΉη»ζ
     */
    @Override
    public ApiResult<?> modifyInfo(IUsersModifyInfoVO modifyInfoVO) {
        IUsersEntity entity = usersEntityRepository
                .findById(modifyInfoVO.getId())
                .orElseThrow(() -> new RuntimeException("η¨ζ·δΈε­ε¨"));
        // ε€ζ­ι?η?±ζ―ε¦εζ³
        if (StringUtils.hasText(modifyInfoVO.getEmail())) {
            boolean match = ReUtil.isMatch("^(([^<>()[\\]\\\\.,;:\\s@\"]+(\\.[^<>()[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))", modifyInfoVO.getEmail());
            if (!match) {
                return new ApiResult<>().error("ι?η?±ζ ΌεΌιθ――");
            } else {
                entity.setEmail(modifyInfoVO.getEmail());
            }
        }
        if (StringUtils.hasText(modifyInfoVO.getAvatar())) {
            entity.setAvatar(modifyInfoVO.getAvatar());
        }
        if (StringUtils.hasText(modifyInfoVO.getUsername()) && !entity.getUsername().equals(modifyInfoVO.getUsername())) {
            if (usersEntityRepository.existsByUsername(modifyInfoVO.getUsername())) {
                return new ApiResult<>().error("η¨ζ·εε·²ε­ε¨");
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
        return new ApiResult<>().success("δΏ?ζΉζ΄ζ°ζε");
    }

    /**
     * ιθΏη¨ζ·εidε ι€η¨ζ·
     *
     * @param id η¨ζ·id
     * @return ε ι€η»ζ
     */
    @Override
    public ApiResult<?> deleteUserById(Integer id) {
        // ε€ζ­ζ―ε¦ε­ε¨εΉΆε ι€
        if (usersEntityRepository.existsById(id)) {
            usersEntityRepository.deleteById(id);
            return new ApiResult<>().success("ε ι€ζε");
        } else {
            return new ApiResult<>().error("η¨ζ·δΈε­ε¨");
        }
    }

    /**
     * ιθΏη¨ζ·εidζ₯θ―’η¨ζ·
     *
     * @param id η¨ζ·id
     * @return ζ₯θ―’η»ζ
     */
    @Override
    public ApiResult<?> getOne(Integer id) {
        IUsersEntity entity = usersEntityRepository.findById(id).orElseThrow(() -> new RuntimeException("η¨ζ·δΈε­ε¨"));
        // ζ°ζ?θ±ζεθΏε

        IUsersBaseInfoDTO result = modelMapper.map(entity, IUsersBaseInfoDTO.class);
        return new ApiResult<IUsersBaseInfoDTO>().success(result);
    }

    /**
     * ιθΏη¨ζ·εζ¨‘η³ζ₯θ―’η¨ζ·
     *
     * @param name     η¨ζ·ε
     * @param pageNum  ι‘΅η 
     * @param pageSize ι‘΅ε€§ε°
     * @return ζ₯θ―’η»ζ
     */
    @Override
    public ApiResult<?> getAllLikeName(String name, Integer pageNum, Integer pageSize) {
        PageRequest pageable = PageRequest.of(pageNum, pageSize);
        Page<IUsersEntity> page = usersEntityRepository.findAllByUsernameLike(name, pageable);
        return getApiResult(page);
    }

    /**
     * θ·εζζη¨ζ·
     *
     * @param pageNum  ι‘΅η 
     * @param pageSize ι‘΅ε€§ε°
     * @return ζ₯θ―’η»ζ
     */
    @Override
    public ApiResult<?> getAll(Integer pageNum, Integer pageSize) {
        PageRequest pageable = PageRequest.of(pageNum, pageSize);
        Page<IUsersEntity> page = usersEntityRepository.findAll(pageable);
        return getApiResult(page);
    }

    private ApiResult<?> getApiResult(Page<IUsersEntity> page) {
        List<IUsersBaseInfoDTO> result = page.getContent().stream()
                .map(e -> modelMapper.map(e, IUsersBaseInfoDTO.class))
                .collect(Collectors.toList());
        PageData.PageDataBuilder<IUsersBaseInfoDTO> builder = PageData.builder();
        return new ApiResult<PageData<IUsersBaseInfoDTO>>().success(builder.totalPage(page.getTotalPages())
                .totalNum(page.getTotalElements())
                .data(result)
                .build());
    }

    /**
     * η»η¨ζ·ζ·»ε η§―ε
     *
     * @param vo ζ·»ε η§―εδΏ‘ζ―
     * @return ζ·»ε η»ζ
     */
    @Override
    public ApiResult<?> addScore(IUsersAddScoreVO vo) {
        IUsersEntity entity = usersEntityRepository.findById(vo.getId()).orElseThrow(() -> new RuntimeException("η¨ζ·δΈε­ε¨"));
        synchronized (this) {
            entity.setScore(entity.getScore() + vo.getScore());
        }
        return new ApiResult<>().success("ζ·»ε η§―εζε");
    }


    /**
     * ζ₯θ―’ζΆθθ?°ε½
     *
     * @param uid η¨ζ·id
     * @return ζ₯θ―’η»ζ
     */
    @Override
    public ApiResult<?> findCollectList(Integer uid) {
        List<ICollectListsEntity> queryResult = collectListsEntityRepository.findAllByUserUid(uid);
        if (CollectionUtils.isEmpty(queryResult)) {
            return new ApiResult<>().warning("ζζ ζΆθθ?°ε½");
        } else {
            return new ApiResult<List<ICollectListsEntity>>().success(queryResult);
        }
    }

    /**
     * ζ₯θ―’εεΈθ?°ε½
     *
     * @param uid η¨ζ·id
     * @return ζ₯θ―’η»ζ
     */
    @Override
    public ApiResult<?> findPublishList(Integer uid) {
        List<IResourcesEntity> queryResult = resourcesEntityRepository.findAllByPublishUserUid(uid);
        if (CollectionUtils.isEmpty(queryResult)) {
            return new ApiResult<>().warning("ζζ εθ‘¨θ?°ε½");
        } else {
            return new ApiResult<List<IResourcesEntity>>().success(queryResult);
        }
    }

    /**
     * ζ₯θ―’εΏζΏε’εθ‘¨θ?°ε½
     *
     * @param uid η¨ζ·id
     * @return ζ₯θ―’η»ζ
     */
    @Override
    public ApiResult<?> findWishList(Integer uid) {
        List<IWishesEntity> queryResult = wishesEntityRepository.findAllByUserUid(uid);
        if (CollectionUtils.isEmpty(queryResult)) {
            return new ApiResult<>().warning("ζζ εΏζΏε’θ?°ε½");
        } else {
            return new ApiResult<List<IWishesEntity>>().success(queryResult);
        }
    }

    /**
     * ζ₯θ―’η§―εθ?°ε½
     *
     * @param uid η¨ζ·id
     * @return ζ₯θ―’η»ζ
     */
    @Override
    public ApiResult<?> findScoreList(Integer uid) {
        List<IScoreListEntity> queryResult = scoreListEntityRepository.findAllByUserUid(uid);
        if (CollectionUtils.isEmpty(queryResult)) {
            return new ApiResult<>().warning("ζζ η§―εθ?°ε½");
        } else {
            return new ApiResult<List<IScoreListEntity>>().success(queryResult);
        }
    }

    /**
     * ζ Ήζ?ζ η­Ύζ₯θ―’η¨ζ·
     *
     * @param labelId  ζ η­Ύid
     * @param pageNum  ι‘΅η 
     * @param pageSize ι‘΅ε€§ε°
     * @return ζ₯θ―’η»ζ
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> findAllByLabelId(List<Integer> labelId, Integer pageNum, Integer pageSize) {
        ArrayList<ILabelsEntity> labels = new ArrayList<>();
        // ζ Ήζ?ζ η­ΎηΌε·ζ₯θ―’ζ η­Ύ
        labelId.forEach(id -> {
            ILabelsEntity labelsEntity = labelsEntityRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("ζ η­ΎδΈε­ε¨"));
            labels.add(labelsEntity);
        });

        PageRequest pageable = PageRequest.of(pageNum, pageSize);

        // ζ₯εΊη¬¦εζ‘δ»Άηη¨ζ·
        Page<IUsersEntity> page = usersEntityRepository.findDistinctAllByLabelsIn(labels, pageable);
        return getApiResult(page);
//        return new ApiResult<>().success(page.getContent());
    }


    @Override
    public ApiResult<?> findAllByNameAndLabelId(String name, List<Integer> labelId, Integer pageNum, Integer pageSize) {
        List<ILabelsEntity> labels = labelsEntityRepository.findAllById(labelId);
        Sort sort = Sort.by(Sort.Direction.ASC, "uid");
        PageRequest pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<IUsersEntity> page;
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
