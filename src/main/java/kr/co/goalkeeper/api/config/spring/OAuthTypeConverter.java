package kr.co.goalkeeper.api.config.spring;

import kr.co.goalkeeper.api.model.oauth.OAuthType;
import org.springframework.core.convert.converter.Converter;

public class OAuthTypeConverter implements Converter<String, OAuthType> {
    @Override
    public OAuthType convert(String source) {
        return OAuthType.valueOf(source.toUpperCase());
    }
}
