package com.sheep.cloud.os.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Zhang Jinming
 * @date 24/11/2022 下午4:35
 */
@Service
@Log4j2
public class OssClientServiceImpl extends AbstractOsStrategyImpl {
    private String url;
    private String accessKeyId;
    private String accessKeySecret;
    private String endpoint;
    private String bucketName;

    @PostConstruct
    public void initApp() throws IOException {
        InputStream stream = getClass().getResourceAsStream("/oss.properties");
        Properties properties = new Properties();
        properties.load(stream);
        bucketName = properties.getProperty("bucketName");
        endpoint = properties.getProperty("endpoint");
        url = "https://" + bucketName + "." + endpoint + "/";
        accessKeyId = properties.getProperty("accessKeyId");
        accessKeySecret = properties.getProperty("accessKeySecret");
    }

    private OSS clientInit() {
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    /**
     * 删除文件
     *
     * @param path 文件路径,eg: user/123456.txt
     */
    @Override
    public void delete(String path) {
        OSS ossClient = clientInit();
        // 删除文件或目录。如果要删除目录，目录必须为空。
        String msg;
        try {
            ossClient.deleteObject(bucketName, path);
            log.info(path + "删除成功!");
        } catch (OSSException oe) {
            msg = "OSS服务异常，请求已发送至OSS服务器，但由于某种原因对方拒绝了请求";
            log.error(msg);
            log.error("Error Message:" + oe.getErrorMessage());
            log.error("Error Code:" + oe.getErrorCode());
            log.error("Request ID:" + oe.getRequestId());
            log.error("Host ID:" + oe.getHostId());
            throw new RuntimeException(msg);
        } catch (ClientException ce) {
            msg = "OSS客户端无法发送请求，请检查本地网络服务或本地配置文件后重试";
            log.error(msg);
            log.error("Error Message:" + ce.getMessage());
            throw new RuntimeException(msg);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    @Override
    public String getFileAccessUrl(String filePath) {
        return url + filePath;
    }

    @Override
    public Boolean exists(String filePath) {
        return clientInit().doesObjectExist(bucketName, filePath);
    }

    @Override
    public void upload(String path, String fileName, InputStream inputStream, String contentType) {
        clientInit().putObject(bucketName, path + fileName, inputStream);
    }

}
