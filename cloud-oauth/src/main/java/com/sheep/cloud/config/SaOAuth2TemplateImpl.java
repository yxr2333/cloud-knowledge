package com.sheep.cloud.config;

import cn.dev33.satoken.oauth2.logic.SaOAuth2Template;
import cn.dev33.satoken.oauth2.model.SaClientModel;
import com.sheep.cloud.dao.IAppClientsEntityRepository;
import com.sheep.cloud.dao.IOAuth2GrantEntityRepository;
import com.sheep.cloud.entity.IAppClientsEntity;
import com.sheep.cloud.entity.IOAuth2GrantEntity;
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
        if (loginId instanceof String) {
            IOAuth2GrantEntity entity = auth2GrantEntityRepository.findByClientIdAndUserUid(Integer.parseInt(clientId), Integer.parseInt((String) loginId));
            return entity.getOpenId();
        } else if (loginId instanceof Integer) {
            IOAuth2GrantEntity entity = auth2GrantEntityRepository.findByClientIdAndUserUid(Integer.parseInt(clientId), (Integer) loginId);
            return entity.getOpenId();
        }
        return "";
    }
}