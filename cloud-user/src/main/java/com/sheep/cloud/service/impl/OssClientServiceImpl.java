package com.sheep.cloud.service.impl;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.sheep.cloud.store.FileUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

/**
 * @author Zhang Jinming
 * @date 24/11/2022 下午4:35
 */
@Service
@Log4j2
public class OssClientServiceImpl {
    /**
     * oss域名
     */
    private String url;

    /**
     * 终点
     */
    private String endpoint;

    /**
     * 访问密钥id
     */
    private String accessKeyId;

    /**
     * 访问密钥密码
     */
    private String accessKeySecret;

    /**
     * bucket名称
     */
    private String bucketName;

    @PostConstruct
    public void initApp() throws IOException {
        InputStream stream = getClass().getResourceAsStream("/oss.properties");
        Properties properties = new Properties();
        properties.load(stream);
        url = "https://" + bucketName + "." + endpoint + "/";
        endpoint = properties.getProperty("endpoint");
        accessKeyId = properties.getProperty("accessKeyId");
        accessKeySecret = properties.getProperty("accessKeySecret");
        bucketName = properties.getProperty("bucketName");
    }

    /**
     * 上传文件
     *
     * @param file MultipartFile格式
     * @param path 上传路径
     *             相对于根目录的路径，路径前不能加/，路径后必须加/
     *             eg: user/elm45678122/
     * @return {@link String} 文件md5值
     */
    public String uploadFileByOss(MultipartFile file, String path) {
        try {

            // 获取文件md5值
            String md5 = FileUtils.getMd5(file.getInputStream());
            // 获取文件扩展名
            String extName = FileUtils.getExtName(file.getOriginalFilename());
            // 重新生成文件名
            String fileName = md5 + extName;
            // 判断文件是否已存在
            if (!exists(path + fileName)) {
                // 不存在则继续上传
                upload(path, fileName, file.getInputStream(), file.getContentType());
            } else {
                throw new RuntimeException("文件已存在失败");
            }
            // 返回文件访问路径
            return getFileAccessUrl(path + fileName);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("文件上传失败");
        }
    }

    /**
     * 删除文件
     *
     * @param url 文件路径
     *            必须是完整的URL, eg: https://aliyun-ossdemo.oss-cn-hangzhou.aliyuncs.com/demo/example.txt
     * @return {@link String} null
     */
    public String deleteFile(String url) {
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        String msg;
        try {
            // 删除文件或目录。如果要删除目录，目录必须为空。
            ossClient.deleteObject(bucketName, new URL(url).getPath());
        } catch (OSSException oe) {
            msg = "OSS服务异常，请求已发送至OSS服务器，但由于某种原因对方拒绝了请求";
            log.error(msg);
            log.error("Error Message:" + oe.getErrorMessage());
            log.error("Error Code:" + oe.getErrorCode());
            log.error("Request ID:" + oe.getRequestId());
            log.error("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            msg = "OSS客户端无法发送请求，请检查本地网络服务或本地配置文件后重试";
            log.error(msg);
            log.error("Error Message:" + ce.getMessage());
            throw new RuntimeException(msg);
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return null;
    }

    private String getFileAccessUrl(String filePath) {
        return url + filePath;
    }

    private Boolean exists(String filePath) {
        return getOssClient().doesObjectExist(bucketName, filePath);
    }

    private void upload(String path, String fileName, InputStream inputStream, String contentType) {
        getOssClient().putObject(bucketName, path + fileName, inputStream);
    }

    private OSS getOssClient() {
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }
}
