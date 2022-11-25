package com.sheep.cloud.store;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * @author Zhang Jinming
 * @create 23/6/2022 下午7:45
 */
@Slf4j
public class RandomHeadImg {
    public static String randomHeadImg() {
        String urlNameString = "http://api.btstu.cn/sjtx/api.php";
        String urlDefaultImg = "https://pic2.zhimg.com/v2-c1abdf0c1a579e5c67a4732923b112a7_1440w.jpg?source=172ae18b";
        try {
            URLConnection connection = new URL(urlNameString).openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            Map<String, List<String>> map = connection.getHeaderFields();
            return map.get("Location").get(0);
        } catch (MalformedURLException e) {
            log.error("URL错误:", e);
        } catch (IOException e) {
            log.error("建立IO连接时出现异常:", e);
        } catch (Exception e) {
            log.error("请求随机头像时发生未知异常:", e);
        }
        return urlDefaultImg;
    }
}
