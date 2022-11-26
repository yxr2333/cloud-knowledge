package com.sheep.cloud.config;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import xyz.erupt.annotation.fun.AttachmentProxy;
import xyz.erupt.core.exception.EruptWebApiRuntimeException;
import xyz.erupt.core.util.MimeUtil;

import java.io.InputStream;

/**
 * Created By IntelliJ IDEA
 *
 * @author IceCreamQAQ
 * @datetime 2022/10/24 星期一
 * Happy Every Coding Time~
 */
@Component
public class UploadLogicProxy implements AttachmentProxy {

    @Value("${qiniu.accessKey}")
    private String accessKey; //你在七牛云申请的ACCESS_KEY

    @Value("${qiniu.secretKey}")
    private String secretKey; //你在七牛云申请的SECRET_KEY

    @Value("${qiniu.bucket}")
    private String bucket; //bucket名称

    @Value("${qiniu.domain}")
    private String domain; //域名

    @Override
    public boolean isLocalSave() {
        return false;
    }

    @Override
    public String upLoad(InputStream inputStream, String path) {
        UploadManager uploadManager = new UploadManager(new Configuration(Region.huanan()));
        String uploadToken = Auth.create(accessKey, secretKey).uploadToken(bucket);
        /*
         *	如果上传地址为 /2020-10-10/erupt.png
         *	在七牛云需通过 http://oos.erupt.xyz//2020-10-10/erupt.png才能访问
         *	访问地址带双斜杠，影响美观，所以做一下处理
         */
        path = path.startsWith("/") ? path.substring(1) : path;
        try {
            Response response = uploadManager.put(inputStream, path, uploadToken, null, MimeUtil.getMimeType(path));
            if (!response.isOK()) {
                throw new EruptWebApiRuntimeException("上传七牛云存储空间失败");
            }
            return "/" + path;
        } catch (QiniuException ex) {
            throw new EruptWebApiRuntimeException(ex.response.toString());
        }
    }

    @Override
    public String fileDomain() {
        return domain;
    }
}
