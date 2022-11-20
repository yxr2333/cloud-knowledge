package com.sheep.cloud.service;

import com.sheep.cloud.dto.response.ApiResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    ApiResult<?> getAllLabelCategoryMenu();


    /**
     * 获取所有标签
     *
     * @return 查询结果
     */
    ApiResult<?> getAllLabels();

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 上传结果
     */
    ApiResult<?> uploadFile(MultipartFile file) throws IOException;

    /**
     * 批量上传文件
     *
     * @param files 文件列表
     * @return 上传结果
     */
    ApiResult<?> uploadFile(MultipartFile[] files);
}
