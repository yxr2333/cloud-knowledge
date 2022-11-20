package com.sheep.cloud.config;

import cn.dev33.satoken.oauth2.logic.SaOAuth2Template;
import cn.dev33.satoken.oauth2.model.SaClientModel;
import cn.hutool.core.util.RandomUtil;
import com.sheep.cloud.dao.knowledge.IAppClientsEntityRepository;
import com.sheep.cloud.dao.knowledge.IOAuth2GrantEntityRepository;
import com.sheep.cloud.entity.knowledge.IAppClientsEntity;
import com.sheep.cloud.entity.knowledge.IOAuth2GrantEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created By Intellij IDEA
 *
 * @author ssssheep
 * @package com.sheep.cloud.config
 * @datetime 2022/9/14 星期三
 */
@Component
@RequiredArgsConstructor
public class SaOAuth2TemplateImpl extends SaOAuth2Template {

    final IOAuth2GrantEntityRepository auth2GrantEntityRepository;
    final IAppClientsEntityRepository appClientsEntityRepository;


    @Override
    public SaClientModel getClientModel(String clientId) {
        Optional<IAppClientsEntity> optional = appClientsEntityRepository.findById(Integer.parseInt(clientId));
        if (optional.isPresent()) {
            IAppClientsEntity entity = optional.get();
            return new SaClientModel()
                    .setClientId(String.valueOf(entity.getClientId()))
                    .setClientSecret(entity.getClientSecret())
                    .setAllowUrl(entity.getAllowUrl())
                    .setContractScope(entity.getContractScope())
                    .setIsAutoMode(entity.getIsAutoMode());
        }
        return null;
    }

    @Override
    public String getOpenid(String clientId, Object loginId) {
        Optional<IOAuth2GrantEntity> optional = auth2GrantEntityRepository
                .findByClientIdAndUserUid(Integer.parseInt(clientId), Integer.parseInt(loginId.toString()));
        if (optional.isPresent()) {
            IOAuth2GrantEntity entity = optional.get();
            if (entity.getOpenId() == null) {
                String openId = RandomUtil.randomString(32);
                entity.setOpenId(openId);
                auth2GrantEntityRepository.save(entity);
                return openId;
            }
            return entity.getOpenId();
        } else {
            return "";
        }
    }
}
