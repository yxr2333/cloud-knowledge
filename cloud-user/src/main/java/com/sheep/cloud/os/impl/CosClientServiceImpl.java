package com.sheep.cloud.os.impl;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author Zhang Jinming
 * @date 24/11/2022 下午5:20
 */
@Service
public class CosClientServiceImpl extends AbstractOsStrategyImpl {
    private String url;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    private String region;

    @PostConstruct
    public void initApp() throws IOException {
        InputStream stream = getClass().getResourceAsStream("/cos.properties");
        Properties properties = new Properties();
        properties.load(stream);
        bucketName = properties.getProperty("bucketName");
        String endpoint = properties.getProperty("endpoint");
        region = properties.getProperty("region");
        url = "https://" + bucketName + "." + endpoint + "/";
        accessKeyId = properties.getProperty("accessKeyId");
        accessKeySecret = properties.getProperty("accessKeySecret");
    }

    @Override
    public void delete(String path) {
        initClient().deleteObject(bucketName, path);
    }

    @Override
    public Boolean exists(String filePath) {
        return initClient().doesObjectExist(bucketName, filePath);
    }

    @Override
    public void upload(String path, String fileName, InputStream inputStream, String contentType) {
        initClient().putObject(bucketName, fileName, inputStream, null);
    }

    @Override
    public String getFileAccessUrl(String filePath) {
        return url + filePath;
    }

    private COSClient initClient() {
        COSCredentials cred = new BasicCOSCredentials(accessKeyId, accessKeySecret);
        ClientConfig clientConfig = new ClientConfig(new Region(region));
        return new COSClient(cred, clientConfig);
    }
}
