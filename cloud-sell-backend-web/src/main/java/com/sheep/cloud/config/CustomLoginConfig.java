package com.sheep.cloud.config;

import com.sheep.cloud.dto.request.knowledge.IUsersLoginVO;
import com.sheep.cloud.dto.response.ApiResult;
import com.sheep.cloud.dto.response.sell.IUserEntityBaseInfo;
import com.sheep.cloud.service.RemoteSellUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.erupt.jpa.dao.EruptDao;
import xyz.erupt.upms.fun.LoginProxy;
import xyz.erupt.upms.model.EruptUser;
import xyz.erupt.upms.service.EruptUserService;

import javax.annotation.Resource;
import java.util.Calendar;

/**
 * Created By IntelliJ IDEA
 *
 * @author IceCreamQAQ
 * @datetime 2022/11/24 星期四
 * Happy Every Coding Time~
 */
@Service
public class CustomLoginConfig implements LoginProxy {

    @Autowired
    private RemoteSellUserService remoteSellUserService;

    @Autowired
    private EruptUserService eruptUserService;
    @Resource
    private EruptDao eruptDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EruptUser login(String account, String pwd) {
        // TODO 校验是不是管理员
        if ("erupt".equals(account)) {
            return eruptDao.queryEntity(EruptUser.class, "account = '" + account + "'");
        }
        IUsersLoginVO loginVO = new IUsersLoginVO(account, pwd);
        ApiResult<IUserEntityBaseInfo> result = remoteSellUserService.doAdminLogin(loginVO);
        if (result.getCode() == 200) {
            EruptUser user = eruptDao.queryEntity(EruptUser.class, "account = '" + account + "'");
            if (user == null) {
                user = new EruptUser();
                user.setAccount(account);
                user.setName(account);
                user.setIsMd5(true);
                user.setIsAdmin(true);
                user.setResetPwdTime(Calendar.getInstance().getTime());
                user.setCreateTime(Calendar.getInstance().getTime());
//                String s1 = MD5Util.digest(pwd) + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + account;
                user.setPassword(pwd);
                eruptDao.persist(user);
                eruptUserService.login(account, pwd);
            }
            return user;
        } else {
            throw new RuntimeException(result.getMsg());
        }

    }

    @Override
    public void loginSuccess(EruptUser eruptUser, String token) {
        LoginProxy.super.loginSuccess(eruptUser, token);
    }

    @Override
    public void logout(String token) {
        LoginProxy.super.logout(token);
    }

    @Override
    public void beforeChangePwd(EruptUser eruptUser, String newPwd) {
        LoginProxy.super.beforeChangePwd(eruptUser, newPwd);
    }

    @Override
    public void afterChangePwd(EruptUser eruptUser, String originPwd, String newPwd) {
        LoginProxy.super.afterChangePwd(eruptUser, originPwd, newPwd);
    }
}
