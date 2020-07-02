package org.aivinog1.feign.resttemplate.client;

import feign.*;
import feign.jackson.JacksonDecoder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

/**
 * @todo #8 Remove this test when starting to implement the main feature (RestTemplate client for Feign).
 *  For now, this is a naive implementation of this
 */
@Disabled
public class SimpleClientTest {
    @Test
    void shouldWorkWithClient() {
        final TestService testService = Feign.builder()
                .decoder(new JacksonDecoder())
                .client(new RestTemplateFeignClient())
                .target(TestService.class, "http://70739.mocklab.io");
        final Map<String, Object> testResult = testService.test("54", "45");
        System.out.println(testResult);
    }
}

class RestTemplateFeignClient implements Client {

    public Response execute(Request request, Request.Options options) throws IOException {
//        return new Client.Default(null, null).execute(request, options);
        final ResponseEntity<byte[]> result = new RestTemplate().exchange(request.url(), HttpMethod.GET, null, byte[].class);
        return Response
                .builder()
                .body(result.getBody())
                .status(result.getStatusCodeValue())
                .request(request)
                .build();
    }
}

interface TestService {

    @RequestLine("GET /coordinates?latitude={latitude}&longitude={longitude}")
    Map<String, Object> test(@Param("latitude") String latitude, @Param("longitude") String longitude);
}
