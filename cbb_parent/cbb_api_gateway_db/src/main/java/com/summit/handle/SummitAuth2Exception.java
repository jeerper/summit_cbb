package com.summit.handle;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * 自定义OAuth2Exception
 *
 * @author Administrator
 */
@JsonSerialize(using = SummitAuth2ExceptionSerializer.class)
public class SummitAuth2Exception extends OAuth2Exception {

    private static final long serialVersionUID = 1448905799897724992L;

    public SummitAuth2Exception(String msg) {

        super(msg);
    }
}
