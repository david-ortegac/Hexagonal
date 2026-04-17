package co.edu.cun.tienda.tienda.application.RestClient;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${external.api.persons.base-url}")
    private String personsBaseUrl;

    @Bean
    public RestClient personsRestClient() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(35));
        factory.setReadTimeout(Duration.ofSeconds(45));

        return RestClient.builder()
                .baseUrl(personsBaseUrl)
                .requestFactory(factory)
                .build();
    }
}
