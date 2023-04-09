package com.seproject.oauth2.converters;

import com.seproject.oauth2.model.ProviderUser;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;

@Component
public class DelegationProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {

    private List<ProviderUserConverter<ProviderUserRequest,ProviderUser>> converters;

    public DelegationProviderUserConverter() {
        this.converters = List.of(new UserDetailsProviderUserConverter(),
                new KakaoProviderUserConverter());
    }

    @Override
    public ProviderUser convert(@NotNull ProviderUserRequest providerUserRequest) {
        for (ProviderUserConverter<ProviderUserRequest, ProviderUser> converter : converters) {
            ProviderUser convert = converter.convert(providerUserRequest);
            if (convert != null) return convert;
        }

        throw new RuntimeException("지원하지 않는 Authorization System");
    }
}
