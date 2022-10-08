package com.sheep.cloud.service;

import com.sheep.cloud.dto.request.SaveOneGoodParam;
import com.sheep.cloud.dto.response.ApiResult;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.service
 * @datetime 2022/9/16 星期五
 */
public interface IGoodsService {


    /**
     * 发布一个商品
     *
     * @param param 商品信息
     * @return 发布结果
     */
    ApiResult saveOne(SaveOneGoodParam param);
}
