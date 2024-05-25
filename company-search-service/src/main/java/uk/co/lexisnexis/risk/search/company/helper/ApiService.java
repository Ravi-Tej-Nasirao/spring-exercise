package uk.co.lexisnexis.risk.search.company.helper;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import uk.co.lexisnexis.risk.search.company.exception.CompanySearchCustomException;

import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;

@Service
public class ApiService<T> {

    @Autowired
    private WebClient webClient;

    public <T> T httpRequest(HttpMethod httpMethod, String apiURL, Map<String, String> params, Map<String, String> headers,
                             String requestBody, MediaType mediaType, Class<T> responseClass, long timeout) {

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(apiURL);
        params.forEach((k, v) -> uriComponentsBuilder.queryParam(k, v));
        URI completeUri = uriComponentsBuilder.build().toUri();

        return webClient
                .method(httpMethod)
                .uri(completeUri)
                .headers(httpHeaders -> {
                    httpHeaders.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
                    if (MapUtils.isNotEmpty(headers)) {
                        for (Map.Entry<String, String> entry : headers.entrySet()) {
                            httpHeaders.set(entry.getKey(), entry.getValue());
                        }
                    }
                })
                .contentType(mediaType)
                .accept(MediaType.ALL)
                .body(BodyInserters.fromValue(Objects.nonNull(requestBody) ? requestBody : ""))
                .retrieve()
                .bodyToMono(responseClass)
                .timeout(Duration.ofSeconds(timeout))
                .onErrorMap(e -> CompanySearchCustomException.builder().backendMessage(e.getMessage()).build())
                .block();
    }

}
