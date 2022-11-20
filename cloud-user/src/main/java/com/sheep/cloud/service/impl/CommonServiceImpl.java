package com.sheep.cloud.service.impl;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.sheep.cloud.dao.knowledge.ILabelCategoryEntityRepository;
import com.sheep.cloud.dao.knowledge.ILabelsEntityRepository;
import com.sheep.cloud.entity.knowledge.ILabelCategoryEntity;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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

    private static final String BASE_URL = "https://insurence-1304011999.cos.ap-shanghai.myqcloud.com/";
    @Autowired
    private ILabelCategoryEntityRepository labelCategoryEntityRepository;

    @Autowired
    private ILabelsEntityRepository labelsEntityRepository;

    @Autowired
    private COSClient cosClient;

    /**
     * 获取所有的标签分类
     *
     * @return 查询结果
     */
    @Override
    public ApiResult getAllLabelCategoryMenu() {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        List<ILabelCategoryEntity> result = labelCategoryEntityRepository.findAll(sort);
        return result.isEmpty() ? new ApiResult<>().warning("查询结果为空") : new ApiResult<>().success(result);
    }

    /**
     * 获取所有标签
     *
     * @return 查询结果
     */
    @Override
    public ApiResult<?> getAllLabels() {
        return new ApiResult<>().success(labelsEntityRepository.findAll());
    }

    /**
     * 上传文件
     *
     * @param file 文件
     * @return 上传结果
     */
    @Override
    public ApiResult<?> uploadFile(MultipartFile file) throws IOException {
        String[] strings = file.getOriginalFilename().split("\\.");
        String bucketName = "insurence-1304011999";
        File tempFile = File.createTempFile(strings[0] + System.currentTimeMillis(), "." + strings[1]);
        System.out.println(tempFile.getName());
        file.transferTo(tempFile);
        String key = tempFile.getName();
        PutObjectRequest request = new PutObjectRequest(bucketName, key, tempFile);
        PutObjectResult result = cosClient.putObject(request);
        return new ApiResult<>().success("上传成功", BASE_URL + key);
    }

    /**
     * 批量上传文件
     *
     * @param files 文件列表
     * @return 上传结果
     */
    @Override
    public ApiResult<?> uploadFile(MultipartFile[] files) {
        return null;
    }
}
