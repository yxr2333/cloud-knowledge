package com.sheep.cloud.service;

import com.sheep.cloud.response.ApiResult;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.service
 * @datetime 2022/8/15 星期一
 */
public interface CommonService {

    /**
     * 获取所有的标签分类
     *
     * @return 查询结果
     */
    ApiResult getAllLabelCategoryMenu();
}
