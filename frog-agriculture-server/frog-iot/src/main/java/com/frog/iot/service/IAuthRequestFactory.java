package com.frog.iot.service;

import com.frog.iot.model.login.AuthRequestWrap;

/**
 * AuthRequest简单工程类接口
 *
 * @author json
 * @date 2022-04-12
 */
public interface IAuthRequestFactory {

    AuthRequestWrap getAuthRequest(String source);

}
