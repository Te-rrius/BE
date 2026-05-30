package hansung.org.terrius.domain.report.analysis.client;

import hansung.org.terrius.domain.report.analysis.dto.AnalyzeMatchReq;
import hansung.org.terrius.domain.report.analysis.dto.AnalyzeResponse;
import hansung.org.terrius.domain.report.analysis.dto.AnalyzeTestFixedCsvReq;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.Map;

@Component
public class FastApiAnalysisClient {

    private static final ParameterizedTypeReference<Map<String, Object>> HEALTH_RESPONSE_TYPE =
            new ParameterizedTypeReference<>() {
            };

    private final RestClient restClient;

    public FastApiAnalysisClient(
            RestClient.Builder restClientBuilder,
            @Value("${fastapi.base-url}") String fastApiBaseUrl,
            @Value("${fastapi.connect-timeout-ms}") long connectTimeoutMs,
            @Value("${fastapi.read-timeout-ms}") long readTimeoutMs
    ) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofMillis(connectTimeoutMs));
        requestFactory.setReadTimeout(Duration.ofMillis(readTimeoutMs));

        this.restClient = restClientBuilder
                .baseUrl(fastApiBaseUrl)
                .requestFactory(requestFactory)
                .build();
    }

    public Map<String, Object> health() {
        return restClient.get()
                .uri("/health")
                .retrieve()
                .body(HEALTH_RESPONSE_TYPE);
    }

    public AnalyzeResponse analyzeMatch(AnalyzeMatchReq req) {
        return restClient.post()
                .uri("/api/v1/analysis/match")
                .body(req)
                .retrieve()
                .body(AnalyzeResponse.class);
    }

    public AnalyzeResponse analyzeMatchWithFixedCsv(AnalyzeTestFixedCsvReq req) {
        return restClient.post()
                .uri("/api/v1/analysis/match/test-fixed-csv")
                .body(req)
                .retrieve()
                .body(AnalyzeResponse.class);
    }
}
