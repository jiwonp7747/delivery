package org.delivery.api.domain.token.ifs;

import org.delivery.api.domain.token.model.TokenDto;

import java.util.Map;

public interface TokenHelperIfs {

    TokenDto issueAcessToken(Map<String, Object> data);

    TokenDto issueRefreshToken(Map<String, Object> data);

    Map<String, Object> validationTokenWithThrow(String token);

}
