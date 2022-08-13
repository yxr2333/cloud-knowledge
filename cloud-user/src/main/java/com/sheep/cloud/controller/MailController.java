package com.sheep.cloud.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.mail.MailUtil;
import com.sheep.cloud.response.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.controller
 * @datetime 2022/8/13 星期六
 */
@Slf4j
@RestController
@RequestMapping("/mail")
public class MailController {

    private static final String FIND_PWD_MAIL_TITLE = "验证找回密码";

    @PostMapping("/sendResetCode")
    public ApiResult sendResetCode(HttpServletRequest request, String email) {
        if (!StringUtils.hasText(email)) {
            return ApiResult.error("请输入邮箱");
        }
        String code = RandomUtil.randomString(5).toUpperCase();
        // 验证码三分钟内有效
        request.getSession().setMaxInactiveInterval(180);
        request.getSession().setAttribute("reset_code", code);
        MailUtil.send(email, FIND_PWD_MAIL_TITLE, "您正在找回密码,验证码为:" + code, false);
        return ApiResult.success("邮件发送成功");
    }
}
