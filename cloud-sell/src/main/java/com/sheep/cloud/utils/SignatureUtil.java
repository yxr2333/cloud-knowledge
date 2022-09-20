package com.sheep.cloud.utils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.utils
 * @datetime 2022/9/19 星期一
 */
@Component
public class SignatureUtil {

    public String makeDingSignature(String timestamp, String secret) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] signatureBytes = mac.doFinal(timestamp.getBytes(StandardCharsets.UTF_8));
        String signature = new String(Base64.encodeBase64(signatureBytes));
        if ("".equals(signature)) {
            return "";
        }
        String encoded = URLEncoder.encode(signature, "UTF-8");
        String urlEncodeSignature = encoded.replace("+", "%20").replace("*", "%2A").replace("~", "%7E").replace("/", "%2F");
        return urlEncodeSignature;
    }
}
