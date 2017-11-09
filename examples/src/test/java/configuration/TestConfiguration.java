package configuration;

import accountinfo.AccountInfoPortType;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Created by Salmondx on 09/09/16.
 */
@Configuration
public class TestConfiguration {

    @Bean
    @Primary
    public AccountInfoPortType test() {
        return Mockito.mock(AccountInfoPortType.class);
    }
}
