package com.sheep.cloud.service.impl;

import com.sheep.cloud.dao.ILabelCategoryEntityRepository;
import com.sheep.cloud.entity.ILabelCategoryEntity;
import com.sheep.cloud.response.ApiResult;
import com.sheep.cloud.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.service.impl
 * @datetime 2022/8/15 星期一
 */
@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private ILabelCategoryEntityRepository labelCategoryEntityRepository;

    /**
     * 获取所有的标签分类
     *
     * @return 查询结果
     */
    @Override
    public ApiResult getAllLabelCategoryMenu() {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        List<ILabelCategoryEntity> result = labelCategoryEntityRepository.findAll(sort);
        return result.isEmpty() ? ApiResult.warning("查询结果为空") : ApiResult.success(result);
    }
}
